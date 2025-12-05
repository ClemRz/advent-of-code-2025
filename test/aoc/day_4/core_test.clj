(ns aoc.day-4.core-test
  (:require [aoc.day-4.core :refer :all]
            [clojure.test :refer :all]))

(def test-input "..@@.@@@@.\n@@@.@.@.@@\n@@@@@.@.@@\n@.@@@@..@.\n@@.@@@@.@@\n.@@@@@@@.@\n.@.@.@.@@@\n@.@@@.@@@@\n.@@@@@@@@.\n@.@.@@@.@.")

(deftest -main-test
  (is (= 13 (-main test-input false)) "Part #1")
  (is (= 43 (-main test-input true)) "Part #2"))
