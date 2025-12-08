(ns aoc.day-7.core-test
  (:require [aoc.day-7.core :refer :all]
            [clojure.test :refer :all]))

(def test-input ".......S.......\n...............\n.......^.......\n...............\n......^.^......\n...............\n.....^.^.^.....\n...............\n....^.^...^....\n...............\n...^.^...^.^...\n...............\n..^...^.....^..\n...............\n.^.^.^.^.^...^.\n...............")

(deftest -main-test
  (is (= 21 (-main test-input false)) "Part #1")
  (is (= 40 (-main test-input true)) "Part #2"))
