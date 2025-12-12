(ns aoc.day-11.core
  (:require [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]))

(def input (slurp-input))
(def out "out")
(def you "you")
(def svr "svr")
(def dac "dac")
(def fft "fft")

(defn parse-line
  [s]
  (str/split s #"(: | )"))

(def parse-input
  (memoize
    (fn [input]
      (->> (str/split-lines input)
           (map parse-line)
           (reduce
             (fn [acc [parent & children]]
               (reduce
                 (fn [acc child]
                   (if (contains? acc child)
                     (update acc child conj parent)
                     (assoc acc child (set [parent]))))
                 acc children))
             {})))))

(def count-steps
  (memoize
    (fn
      [ancestors-map start device counter dac? fft?]
      (let [ancestors (get ancestors-map device)
            you? (= start you)
            dac? (or you? (or dac? (= device dac)))
            fft? (or you? (or fft? (= device fft)))]
        (cond
          (or (nil? ancestors) (= start device)) 0
          (and (ancestors start) dac? fft?) 1
          :else (reduce
                  (fn [acc ancestor]
                    (let [value (count-steps ancestors-map start ancestor counter dac? fft?)]
                      (+ acc value)))
                  counter ancestors))))))

(defn -main
  [input start]
  (-> (parse-input input)
      (count-steps start out 0 false false)))

(comment
  ;; Part #1: 719
  ;; 6.885208 msecs
  (time (-main input you))

  ;; Part #2: 337433554149492
  ;; 13.054916 msecs
  (time (-main input svr))
  )
