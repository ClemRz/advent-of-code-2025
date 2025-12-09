(ns aoc.day-8.core
  (:require [aoc.utils :refer [slurp-input]]
            [clojure.math :as math]
            [clojure.string :as str]))

(def input (slurp-input))

(def parse-input
  (memoize
    (fn [input]
      (let [pattern #","]
        (->> (str/split-lines input)
             (map #(map parse-long (str/split % pattern))))))))

(defn euclidian-distance
  [[a b]]
  (math/sqrt (reduce + (map (comp #(math/pow % 2) -) b a))))

(defn add-distance
  [distance-map boxes]
  (assoc distance-map (euclidian-distance boxes) boxes))

(defn intersecting-indexes
  "Returns the list of the circuit indexes where the boxes were found."
  [circuits boxes]
  (-> (reduce
        (fn [{:keys [i] :as acc} circuit]
          (cond-> (update acc :i inc)
                  (some circuit boxes) (update :idxs conj i)))
        {:i    0
         :idxs (list)}
        circuits)
      :idxs))

(defn merge'
  [circuits idxs to-idx]
  (reduce
    (fn [circuits idx]
      (-> (update circuits to-idx #(apply conj % (nth circuits idx)))
          (update idx (constantly #{}))))
    circuits
    idxs))

(defn connect
  "Returns the circuits after connecting all the boxes"
  [circuits boxes]
  (let [boxes (set boxes)
        [to-idx & idxs] (intersecting-indexes circuits boxes)]
    (if to-idx
      (-> (update circuits to-idx #(apply conj % boxes))
          (merge' idxs to-idx))
      (conj circuits boxes))))

(defn distance-map
  [boxes]
  (->> boxes
       (reduce
         (fn [{:keys [boxes] :as acc} box-a]
           (-> (update acc :distance-map #(reduce
                                            (fn [distance-map box-b]
                                              (add-distance distance-map (list box-a box-b)))
                                            % boxes))
               (update :boxes rest)))
         {:distance-map {}
          :boxes        (rest boxes)})
       :distance-map))

(defn part-1
  [distance-map distances pairs]
  (->> (take pairs distances)
       (reduce
         (fn [circuits dst]
           (connect circuits (get distance-map dst)))
         [])
       (map count)
       (sort >)
       (take 3)
       (reduce *)))

(defn part-2
  [distance-map distances boxes]
  (let [boxes-count (count boxes)]
    (->> distances
         (reduce
           (fn [circuits dst]
             (let [boxes (get distance-map dst)
                   circuits (connect circuits boxes)]
               (if (= boxes-count (transduce (map count) max 0 circuits))
                 (reduced boxes)
                 circuits)))
           [])
         (transduce (map first) *))))

(defn -main
  [input & [pairs]]
  (let [boxes (parse-input input)
        distance-map (distance-map boxes)
        distances (sort (keys distance-map))]
    (if (some? pairs)
      (part-1 distance-map distances pairs)
      (part-2 distance-map distances boxes))))

(comment
  ;; Part #1: 103488
  ;; 556.829375 msecs
  (time (-main input 1000))

  ;; Part #2: 8759985540
  ;; 758.230375 msecs
  (time (-main input))
  )
