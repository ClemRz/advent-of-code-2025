(ns aoc.day-4.core
  (:require [aoc.utils :refer [neighbors parse-matrix print-matrix slurp-input]]
            [clojure.string :as str]))

(def input (slurp-input))
(def roll_char \@)
(def cross_char \x)

(def parse-input
  (memoize
    (fn [input]
      (parse-matrix (str/split-lines input)))))

(defn roll?
  [directory p]
  (= (get directory p) roll_char))

(defn roll-count
  [directory neighbors]
  (reduce
    (fn [counter p]
      (cond-> counter
              (roll? directory p) inc))
    0 neighbors))

(defn accessible?
  [directory neighbors]
  (< (roll-count directory neighbors) 4))

(defn forklift-it
  [state pos]
  (-> state
      (update :x conj pos)
      (update :rolls dissoc pos)
      (assoc-in [:directory pos] cross_char)
      (assoc :found? true)))

(defn move
  [directory with-removal? rolls]
  (loop [directory directory
         rolls rolls
         x (list)]
    (let [{:keys [directory rolls x found?]}
          (reduce
            (fn [acc [pos neighbors]]
              (cond-> acc
                      (accessible? directory neighbors) (forklift-it pos)))
            {:directory directory
             :rolls     rolls
             :x         x
             :found?    false}
            rolls)]
      (if (and with-removal? found?)
        (recur directory rolls x)
        {:x         x
         :directory directory}))))

(defn -main
  [input with-removal?]
  (let [{:keys [index directory width height]} (parse-input input)
        {:keys [directory x]}
        (->> (get index roll_char)
             (reduce
               (fn [rolls pos]
                 (assoc rolls pos (neighbors pos width height)))
               {})
             (move directory with-removal?))]
    (comment (print-matrix directory width height))
    (count x)))

(comment
  ;; Part #1: 1419
  ;; 128.520375 msecs
  (time (-main input false))

  ;; Part #2: 8739
  ;; 594.769209 msecs
  (time (-main input true))
  )
