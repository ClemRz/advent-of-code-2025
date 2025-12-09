(ns aoc.day-9.core-test
  (:require [aoc.day-9.core :refer :all]
            [clojure.test :refer :all]))

(def test-input "7,1\n11,1\n11,7\n9,7\n9,5\n2,5\n2,3\n7,3")

(deftest -main-test
  (is (= 50 (-main test-input false)) "Part #1")
  (is (= 24 (-main test-input true)) "Part #2"))
