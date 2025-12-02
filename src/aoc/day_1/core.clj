(ns aoc.day-1.core
  (:require [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]))

(def input (slurp-input))

(def dial-start 50)
(def dial-size 100)

(def parse-input
  (memoize
    (fn [input]
      (->> (str/split-lines input)
           (reduce
             (fn [acc s]
               (let [[letter & number] s]
                 (conj acc [(case letter \L - \R +)
                            (parse-long (str/join number))])))
             [])))))

(defn origin-stops
  "Returns 1 if the next position lands on the origin of the dial, 0 otherwise."
  [& {:keys [next-pos]}]
  (if (zero? (mod next-pos dial-size))
    1
    0))

(defn origin-clicks
  "Returns how many times the dial clicks at the origin."
  [& {:keys [last-pos n f]}]
  (let [pos (mod last-pos dial-size)
        dst-to-origin (if (= - f)
                        pos
                        (rem (- dial-size pos) dial-size))]
    (if (>= n dst-to-origin)
      (cond-> (quot (- n dst-to-origin) dial-size)
              (not (zero? pos)) inc)
      0)))

(defn -main
  "Returns the password."
  [input count-impl]
  (->> (parse-input input)
       (reduce (fn [[[last-pos counter] :as acc] [f n]]
                 (let [next-pos (f last-pos n)]
                   (conj acc [next-pos (+ counter (count-impl :last-pos last-pos :next-pos next-pos :n n :f f))])))
               (list [dial-start 0]))
       first
       last))

(comment
  ;; Part #1: 992
  ;; 24.585333 msecs
  (time (-main input origin-stops))

  ;; Part #2: 6133
  ;; 3.865584 msecs
  (time (-main input origin-clicks))
  )
