(ns aoc.day-5.core-test
  (:require [aoc.day-5.core :refer :all]
            [clojure.test :refer :all]))

(def test-input "3-5\n10-14\n16-20\n12-18\n\n1\n5\n8\n11\n17\n32")

(deftest -main-test
  (is (= 3 (-main test-input true)) "Part #1")
  (is (= 14 (-main test-input false)) "Part #2"))
