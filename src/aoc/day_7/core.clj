(ns aoc.day-7.core
  (:require [aoc.utils :refer [parse-matrix print-matrix slurp-input]]
            [clojure.string :as str]))

(def input (slurp-input))

(def parse-input (memoize (comp parse-matrix str/split-lines)))

(def beam \|)
(def splitter \^)

(defn turn-on
  [directory position]
  (assoc directory position beam))

(defn +''
  [a b]
  (+ (or a 0) b))

(defn light-up
  [acc [x :as pos] weight]
  (-> acc
      (update :directory turn-on pos)
      (update-in [:beams x pos] +'' weight)))

(defn step
  [x {:keys [directory] :as acc} [[_ ib-y] weight]]
  (let [pos [x ib-y]]
    (if (= (get directory pos) splitter)
      (-> acc
          (light-up [x (dec ib-y)] weight)
          (light-up [x (inc ib-y)] weight)
          (update :split-count inc))
      (light-up acc pos weight))))

(defn -main
  [input quantum?]
  (let [{:keys [index directory width height]} (parse-input input)
        [start-x start-y] (first (get index \S))
        first-beam-x (inc start-x)]
    (let [{:keys [beams directory split-count]}
          (->> (range (inc first-beam-x) height)
               (reduce
                 (fn [{:keys [beams] :as acc} x]
                   (println)
                   (let [incoming-beams (get beams (dec x))]
                     (reduce (partial step x)
                             (update acc :beams assoc x {})
                             incoming-beams)))
                 (light-up {:directory   directory
                            :beams       {first-beam-x {}}
                            :split-count 0}
                           [first-beam-x start-y]
                           1)))]
      (print-matrix directory width height)
      (if quantum?
        (->> (dec height)
             (get beams)
             vals
             (reduce +))
        split-count))))

(comment
  ;; Part #1: 1626
  ;; 43.635083 msecs
  (time (-main input false))

  ;; Part #2: 48989920237096
  ;; 179.104709 msecs
  (time (-main input true))
  )
