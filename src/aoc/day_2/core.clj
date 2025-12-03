(ns aoc.day-2.core
  (:require [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]))

(def input (slurp-input))

(def parse-input
  (memoize
    (fn [input]
      (->> (str/split input #"[-,]")
           (partition-all 2)
           (reduce
             (fn [acc pair]
               (let [[a b] (map parse-long pair)]
                 (into acc (map (juxt identity str) (range a (inc b))))))
             (list))))))

(defn repeated-twice?
  [s]
  (let [c (count s)]
    (and (even? c)
         (apply = (split-at (/ c 2) s)))))

(def divisors
  (memoize
    (fn [n]
      (filter #(zero? (rem n %)) (range 1 (inc (/ n 2)))))))

(defn repeated-n-times?
  [s]
  (let [c (count s)]
    (and (> c 1)
         (->> (divisors c)
              (some #(apply = (partition % s)))))))

(defn -main
  "Returns the sum of invalid IDs."
  [input pred]
  (->> (parse-input input)
       (reduce
         (fn [acc [d s]]
           (cond-> acc
                   (pred s) (+ d)))
         0)))

(comment
  ;; Part #1: 18893502033
  ;; 1497.641333 msecs
  (time (-main input repeated-twice?))

  ;; Part #2: 26202168557
  ;; 5509.33925 msecs
  (time (-main input repeated-n-times?))
  )
