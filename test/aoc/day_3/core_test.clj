(ns aoc.day-3.core-test
  (:require [aoc.day-3.core :refer :all]
            [clojure.test :refer :all]))

(def test-input "987654321111111\n811111111111119\n234234234234278\n818181911112111")

(deftest joltage-test
  (is (= 98 (joltage 2 [9 8 7 6 5 4 3 2 1 1 1 1 1 1 1])))
  (is (= 89 (joltage 2 [8 1 1 1 1 1 1 1 1 1 1 1 1 1 9])))
  (is (= 98 (joltage 2 [9 1 1 1 1 1 1 1 1 1 1 1 1 1 8])))
  (is (= 98 (joltage 2 [9 1 1 1 1 1 1 1 1 1 1 1 1 8 1])))
  (is (= 78 (joltage 2 [2 3 4 2 3 4 2 3 4 2 3 4 2 7 8])))
  (is (= 92 (joltage 2 [8 1 8 1 8 1 9 1 1 1 1 2 1 1 1])))
  (is (= 921 (joltage 3 [8 1 8 1 8 1 9 1 1 1 1 2 1 1 1])))
  (is (= 929 (joltage 3 [8 1 8 1 8 1 9 1 1 1 1 2 1 1 9])))
  (is (= 789 (joltage 3 [3 1 4 1 5 1 6 1 1 1 1 2 7 8 9])))
  (is (= 9211 (joltage 4 [8 1 8 1 8 1 9 1 1 1 1 2 1 1 1])))
  (is (= 9219 (joltage 4 [8 1 8 1 8 1 9 1 1 1 1 2 1 1 9])))
  (is (= 6789 (joltage 4 [3 1 4 1 5 1 6 1 1 1 1 2 7 8 9])))
  (is (= 6789 (joltage 4 [3 1 4 1 5 1 4 1 1 1 1 6 7 8 9]))))

(deftest -main-test
  (is (= 357 (-main test-input 2)) "Part #1")
  (is (= 3121910778619 (-main test-input 12)) "Part #2"))
