(ns aoc.utils
  (:require [clojure.java.io :as io]
            [criterium.core :as crit]))

(defn slurp-input
  []
  (-> *ns* (str ".txt") io/resource slurp))

(defn print-matrix
  [directory width height]
  (println
    (reduce
      (fn [acc x]
        (-> (reduce
              (fn [acc y]
                (str acc (get directory [x y])))
              acc (range 0 width))
            (str "\n")))
      "" (range 0 height))))

(def neighbors
  (memoize
    (fn [[x y :as pos] width height]
      (reduce
        (fn [acc [x y :as p]]
          (cond-> acc
                  (and (not-any? neg? p)
                       (< x width)
                       (< y height)
                       (not= pos p)) (conj p)))
        (list)
        (for [x (range (dec x) (+ x 2))
              y (range (dec y) (+ y 2))]
          [x y])))))

(defn parse-row
  [r chr-row]
  (map-indexed (fn [i c] {:chr c :pos [r i]}) chr-row))

(defn index
  [matrix]
  (reduce
    (fn [acc {:keys [chr pos]}]
      (update acc chr conj pos))
    {} matrix))

(defn directory
  [matrix]
  (reduce
    (fn [acc {:keys [pos chr]}]
      (assoc acc pos chr))
    {} matrix))

(defn parse-matrix
  [lines]
  (-> (->> lines
           (map-indexed parse-row)
           (mapcat identity)
           ((juxt identity index directory))
           (zipmap [:matrix :index :directory]))
      (assoc :width (-> lines first count)
             :height (count lines))))

(defn bench
  [x]
  (crit/with-progress-reporting (crit/quick-bench x)))