(ns aoc.day-2.core-test
  (:require [aoc.day-2.core :refer :all]
            [clojure.test :refer :all]))

(def test-input "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124")

(deftest -main-test
  (is (= 1227775554 (-main test-input repeated-twice?)) "Part #1")
  (is (= 4174379265 (-main test-input repeated-n-times?)) "Part #2"))
