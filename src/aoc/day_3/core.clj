(ns aoc.day-3.core
  (:require [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]))

(def input (slurp-input))

(def parse-input
  (memoize
    (fn [input]
      (let [pattern #"\d"]
        (->> (str/split-lines input)
             (map #(map parse-long (re-seq pattern %))))))))

(def powers-of-ten
  (memoize
    (fn [n]
      (take n (iterate (partial * 10) 1)))))

(defn measurements->joltage
  [digit-count measurements]
  (->> (powers-of-ten digit-count)
       (map * measurements)
       (reduce +)))

(defn qualifies?
  "Digits looks like [LSD ... MSD]"
  [digits voltage position max-pos digit-count digit-pos]
  (and
    ;; There are still seats left for the rest of the digits
    (<= position (- max-pos digit-pos))
    (or
      ;; This is the least significant digit
      (>= (inc digit-pos) digit-count)
      ;; The next higher significant digit is set
      (pos? (nth digits (inc digit-pos))))
    ;; The voltage is higher than the previous measurement
    (> voltage (nth digits digit-pos))))

(defn update-voltage
  [digits voltage digit-count zeros digit-pos]
  (concat
    ;; Reset the lesser significant digits back to zero
    (take digit-pos zeros)
    ;; Set the new measurement
    [voltage]
    ;; Carry over the greater significant digits
    (take-last (- digit-count digit-pos 1) digits)))

(defn adapter
  [digit-pos [digits voltage position max-pos digit-count zeros]]
  (when (qualifies? digits voltage position max-pos digit-count digit-pos)
    (update-voltage digits voltage digit-count zeros digit-pos)))

(def adapters
  (memoize
    (fn [digit-count]
      (map
        (fn [digit-pos] (partial adapter digit-pos))
        (reverse (range digit-count))))))

(defn battery-meter
  [digits voltage position max-pos digit-count zeros]
  (or
    ((apply some-fn (adapters digit-count)) [digits voltage position max-pos digit-count zeros])
    digits))

(defn joltage
  "Joltage transducer"
  [digit-count bank]
  (let [bat-count (count bank)
        zeros (vec (repeat digit-count 0))]
    (->> (range bat-count)
         (reduce
           (fn [digits position]
             (battery-meter digits (nth bank position) position (dec bat-count) digit-count zeros))
           zeros)
         (measurements->joltage digit-count))))

(defn -main
  "Returns the total joltage."
  [input digit-count]
  (->> (parse-input input)
       (transduce (map #(joltage digit-count %)) +)))

(comment
  ;; Part #1: 17158
  ;; 36.74025 msecs
  (time (-main input 2))

  ;; Part #2: 170449335646486
  ;; 78.452542 msecs
  (time (-main input 12))
  )
