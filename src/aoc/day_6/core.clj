(ns aoc.day-6.core
  (:require [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]))

(def input (slurp-input))
(def pattern #"\s+")
(def str-operator-map {"+" +
                       "-" -
                       "*" *
                       "/" /})
(def chr-operator-map {\+ +
                       \- -
                       \* *
                       \/ /})

(defn parse-line-like-a-human
  [line]
  (->> (str/split line pattern)
       (reduce
         (fn [acc s]
           (cond-> acc
                   (not (str/blank? s)) (conj s)))
         (list))))

(def parse-input (memoize str/split-lines))

(def operator? (memoize (partial contains? str-operator-map)))

(defn human-operation
  [lines main-acc i]
  (->> (vec lines)
       (reduce-kv
         (fn [acc j line]
           (let [item (nth line i)]
             (if (operator? item)
               (reduced (+ main-acc (apply (get str-operator-map item) acc)))
               (let [n (parse-long item)]
                 (cond
                   (zero? j) (list n)
                   :else (conj acc n))))))
         main-acc)))

(defn solve-like-a-human
  [lines]
  (let [lines (map parse-line-like-a-human lines)
        c (-> lines first count)]
    (reduce (partial human-operation lines) 0 (range c))))

(defn start-new-operation
  [m digits]
  (update m :acc conj {:operator (get chr-operator-map (first digits))
                       :operands (repeat 4 (list))}))

(defn stack-operands
  [m digits operation-index]
  (update-in m [:acc operation-index :operands] #(map conj % digits)))

(defn process
  [{:keys [operation-index] :as m} line-number line-count digits]
  (if (= line-number (dec line-count))
    (start-new-operation m digits)
    (stack-operands m digits operation-index)))

(defn process-line
  [line-count line-number {:keys [line] :as m} size]
  (-> m
      (process line-number line-count (take (dec size) line))
      (update :line #(drop size %))
      (update :operation-index inc)))

(defn transpose
  [line-count lines sizes]
  (->> (reverse (range line-count))
       (reduce
         (fn [acc line-number]
           (:acc
             (reduce
               (partial process-line line-count line-number)
               {:line            (nth lines line-number)
                :operation-index 0
                :acc             acc}
               sizes)))
         [])))

(defn operand->long
  [coll]
  (-> (apply str coll)
      str/trim
      parse-long))

(defn solve-like-a-cephalopod
  [lines]
  (->> (str (last lines) " ")
       (re-seq #"[^\s]\s+")
       (map count)
       (transpose (count lines) lines)
       (reduce
         (fn [acc {:keys [operator operands]}]
           (+ acc
              (reduce operator (map operand->long operands))))
         0)))

(defn -main
  [input solve]
  (solve (parse-input input)))

(comment
  ;; Part #1: 5977759036837
  ;; 32.98 msecs
  (time (-main input solve-like-a-human))

  ;; Part #2: 9630000828442
  ;; 24.215792 msecs
  (time (-main input solve-like-a-cephalopod))
  )
