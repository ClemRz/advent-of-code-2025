(ns aoc.day-6.core-test
  (:require [aoc.day-6.core :refer :all]
            [clojure.test :refer :all]))

(def test-input "123 328  51 64 \n 45 64  387 23 \n  6 98  215 314\n*   +   *   +  ")

(deftest -main-test
  (is (= 4277556 (-main test-input solve-like-a-human)) "Part #1")
  (is (= 3263827 (-main test-input solve-like-a-cephalopod)) "Part #2"))
