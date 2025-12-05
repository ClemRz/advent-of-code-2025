(ns aoc.day-5.core
  (:require [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]))

(def input (slurp-input))

(defn parse-range
  [s]
  (map parse-long (str/split s #"-")))

(defn contains?
  [[a b] x]
  (<= a x b))

(defn contains?-fn
  "HOF"
  [r]
  (partial contains? r))

(defn shrink
  [ranges range]
  (reduce
    (fn [[a b :as r] [a' b' :as r']]
      (let [a-inside? (contains? r' a)
            b-inside? (contains? r' b)]
        (cond
          (and a-inside? b-inside?) (reduced nil)
          a-inside? [(inc b') b]
          b-inside? [a (dec a')]
          :else r)))
    range ranges))

(defn cleanup
  [ranges]
  (loop [ranges ranges]
    (let [{:keys [ranges recur?]}
          (->> ranges
               (sort-by (juxt first second))
               (reduce
                 (fn [{:keys [ranges recur?]} range]
                   (let [r (shrink ranges range)]
                     {:ranges (cond-> ranges
                                      (some? r) (conj r))
                      :recur? (or recur? (not= r range))}))
                 {:ranges (list)
                  :recur? false}))]
      (if recur?
        (recur ranges)
        ranges))))

(defn count-fresh
  [ranges]
  (reduce
    (fn [counter [a b]]
      (+ counter (inc (- b a))))
    0 ranges))

(def parse-input
  (memoize
    (fn [input]
      (let [[fresh [_ & available]] (->> (str/split-lines input)
                                         (split-with (complement str/blank?)))]
        {:fresh-ranges (cleanup (map parse-range fresh))
         :available    (map parse-long available)}))))

(defn fresh?
  [fresh-ranges]
  (apply some-fn (map contains?-fn fresh-ranges)))

(defn -main
  [input available-only?]
  (let [{:keys [available fresh-ranges]} (parse-input input)]
    (if available-only?
      (count (filter (fresh? fresh-ranges) available))
      (count-fresh fresh-ranges))))

(comment
  ;; Part #1: 558
  ;; 59.40125 msecs
  (time (-main input true))

  ;; Part #2: 344813017450467
  ;; 0.55425 msecs
  (time (-main input false))
  )
