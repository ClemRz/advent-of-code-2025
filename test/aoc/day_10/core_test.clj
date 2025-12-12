(ns aoc.day-10.core-test
  (:require [aoc.day-10.core :refer :all]
            [clojure.test :refer :all]))

(def test-input "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}\n[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}\n[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}")

(deftest -main-test
  (is (= 7 (-main test-input false)) "Part #1")
  (is (= 33 (-main test-input true)) "Part #2"))
