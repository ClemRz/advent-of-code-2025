(ns aoc.day-9.core
  (:require [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]))

(def input (slurp-input))

(def parse-input
  (memoize
    (fn [input]
      (let [pattern #","]
        (->> (str/split-lines input)
             (map #(map parse-long (str/split % pattern))))))))

(defn area
  [[a b]]
  (reduce * (map (comp inc abs -) a b)))

(defn outside?
  "Returns true if range n'-N' is outside range n-N"
  [r [n N]]
  (let [[n' N'] (sort r)]
    (or (>= n' N)
        (<= N' n))))

(defn crossing?
  "Returns true if the leg crosses the rectangle, false otherwise."
  [[[x'1 y'1] [x'2 y'2]] [x X :as xs] [y Y :as ys]]
  (cond
    (= x'1 x'2) (and (< x x'1 X) (not (outside? [y'1 y'2] ys)))
    (= y'1 y'2) (and (< y y'1 Y) (not (outside? [x'1 x'2] xs)))))

(defn fully-green?
  [legs [tile-after-a] [a b]]
  (or (= tile-after-a b)
      (let [[xs ys] (map (comp sort vector) a b)]
        (not-any? #(crossing? % xs ys) legs))))

(defn add-area
  [area-map corners]
  (assoc area-map (area corners) corners))

(defn area-map
  [green? all-tiles]
  (let [legs (conj (partition 2 1 all-tiles) (list (first all-tiles) (last all-tiles)))]
    (->> all-tiles
         (reduce
           (fn [{:keys [tiles] :as acc} box-a]
             (-> (update acc :area-map
                         #(reduce
                            (fn [area-map box-b]
                              (let [rectangle (list box-a box-b)]
                                (cond-> area-map
                                        (or (not green?)
                                            (fully-green? legs tiles rectangle)) (add-area rectangle))))
                            % tiles))
                 (update :tiles rest)))
           {:area-map {}
            :tiles    (rest all-tiles)})
         :area-map)))

(defn -main
  [input green?]
  (->> (parse-input input)
       (area-map green?)
       (transduce (map first) max 0)))

(comment
  ;; Part #1: 4752484112
  ;; 172.429625 msecs
  (time (-main input false))

  ;; Part #2: 1465767840
  ;; 9318.030709 msecs
  (time (-main input true))
  )
