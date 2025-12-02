(ns aoc.day-1.core-test
  (:require [aoc.day-1.core :refer :all]
            [clojure.test :refer :all]))

(def test-input "L68\nL30\nR48\nL5\nR60\nL55\nL1\nL99\nR14\nL82")

(deftest -main-test
  (is (= 3 (-main test-input origin-stops)) "Part #1")
  (is (= 6 (-main test-input origin-clicks)) "Part #2"))
