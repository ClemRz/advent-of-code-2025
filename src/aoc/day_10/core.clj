(ns aoc.day-10.core
  (:require [aoc.utils :refer [slurp-input]]
            [clojure.core.matrix :as m]
            [clojure.math :as math]
            [clojure.math.combinatorics :as combo]
            [clojure.string :as str]))

(def input (slurp-input))
(def on \#)
(def off \.)

(defn parse-diagram
  [s]
  (let [chars (rest (butlast s))]
    (->> (range (count chars))
         (reduce
           (fn [d i]
             (cond-> d
                     (= on (nth chars i)) (+ (math/pow 2 i))))
           0)
         int)))

(defn unparse-diagram
  [s]
  (reduce
    (fn [acc c]
      (str (or (and (= \1 c) on) off) acc))
    "" s))

(defn parse-numbers
  [s]
  (map parse-long (re-seq #"\d+" s)))

(defn parse-schematic
  [as-vector? digits]
  (if as-vector?
    digits
    (->> digits
         (transduce (map (partial math/pow 2)) +)
         int)))

(defn unparse-schematic
  [s]
  (->> (range (count s))
       (reduce
         (fn [acc i]
           (cond-> acc
                   (= \1 (nth s i)) (conj i)))
         [])
       (str/join ",")))

(defn unparse
  [coll]
  (let [[light-diagram pressed-buttons] (map Integer/toBinaryString coll)]
    [(unparse-diagram light-diagram) (map unparse-schematic pressed-buttons)]))

(def parse-input
  (memoize
    (fn [input schematics-vector?]
      (let [pattern #"\s"]
        (->> (str/split-lines input)
             (transduce
               (map #(str/split % pattern))
               (completing
                 (fn [acc [diagram & rest]]
                   (conj acc {:light-diagram        (parse-diagram diagram)
                              :button-schematics    (map (comp (partial parse-schematic schematics-vector?) parse-numbers) (butlast rest))
                              :joltage-requirements (parse-numbers (last rest))})))
               (list)))))))

(def press-button bit-xor)

(defn press-buttons
  [light-diagram button-schematics light-state-map light-state pressed-buttons]
  (reduce
    (fn [light-state-map button-schematic]
      (let [next-light-state (press-button light-state button-schematic)
            next-pressed-buttons (conj pressed-buttons button-schematic)
            next-light-state-map (-> light-state-map
                                     (assoc next-pressed-buttons next-light-state)
                                     (dissoc pressed-buttons))]
        (if (= next-light-state light-diagram)
          (reduced (assoc next-light-state-map :solution [light-diagram next-pressed-buttons]))
          next-light-state-map)))
    light-state-map button-schematics))

(defn process-level
  [light-diagram button-schematics light-state-map]
  (reduce
    (fn [light-state-map [pressed-buttons light-state]]
      (let [{:keys [solution] :as next-light-state-map} (press-buttons light-diagram button-schematics light-state-map light-state pressed-buttons)]
        (if solution
          (reduced next-light-state-map)
          next-light-state-map)))
    {} light-state-map))

(defn solve-lights
  "Returns the light matching solution as a vector [light-diagram, (button-schematic, button-schematic, ...)]"
  [{:keys [light-diagram button-schematics]}]
  (loop [light-state-map {(list) 0}]
    (let [{:keys [solution] :as next-light-state-map} (process-level light-diagram button-schematics light-state-map)]
      (or solution
          (recur next-light-state-map)))))

(defn schematic-matrix
  [button-schematics m n]
  (reduce
    (fn [M i]
      (->> (reduce
             (fn [row j]
               (conj row (if ((set (nth button-schematics j)) i) 1 0)))
             [] (range n))
           (conj M)))
    [] (range m)))

(defn row-variations
  "Returns a lazy seq of all the possible square matrices removing m-n rows from C"
  [C m n]
  (->> (combo/combinations (range m) n)
       (map #(m/select C % :all))))

(defn column-variations
  "Returns a lazy seq of all the possible square matrices removing n-m columns from C"
  [C m n]
  (->> (combo/combinations (range n) m)
       (map #(m/select C :all %))))

(defn build-As
  [M m n]
  (cond
    (> m n) (list M) #_(row-variations M m n)
    (= m n) (list M)
    (< m n) (column-variations M m n)))

(def all-int-vectors
  (memoize
    (fn [n max]
      (apply combo/cartesian-product
             (repeat n (range (inc max)))))))

(defn slow-solve
  [A n B]
  (let [B (map #(+ 0.0 %) B)]
    (for [x (all-int-vectors n 6)
          :let [x (vec x)]
          :when (= (m/mmul A x) B)]
      x)))

(defn fast-solve
  "Returns A-1.B if |A| is not zero, nil otherwise."
  [A B]
  (try
    (some-> (m/inverse A)
           (m/mmul B))
    (catch Exception _)))

(defn valid?
  [solution]
  (some->> solution (not-any? neg?)))

(defn brute-force-if-needed
  [M n joltage-requirements solutions]
  (println (count solutions) "solutions")
  (if (empty? solutions)
    (->> (slow-solve M n joltage-requirements)
         (filter valid?))
    solutions))

(defn solve-joltage
  [{:keys [joltage-requirements button-schematics]}]
  (let [m (transduce (map (comp inc last)) max 0 button-schematics)
        n (count button-schematics)
        M (schematic-matrix button-schematics m n)]
    (println m "x" n)
    (->> (build-As M m n)
         (map #(fast-solve % joltage-requirements))
         (filter valid?)
         (brute-force-if-needed M n joltage-requirements)
         (transduce
           (map (comp int (partial reduce +)))
           min ##Inf))))

(defn -main
  [input joltage?]
  (let [input (parse-input input joltage?)]
    (if joltage?
      (transduce (map solve-joltage) + input)
      (transduce (map (comp count second solve-lights)) + input))))

(comment
  ;; Part #1: 500
  ;; 3646.320709 msecs
  (time (-main input false))

  ;; Part #2:
  ;;
  (time (-main input true))
  )
