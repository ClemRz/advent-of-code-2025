(ns aoc.day-8.core-test
  (:require [aoc.day-8.core :refer :all]
            [clojure.test :refer :all]))

(def test-input "162,817,812\n57,618,57\n906,360,560\n592,479,940\n352,342,300\n466,668,158\n542,29,236\n431,825,988\n739,650,466\n52,470,668\n216,146,977\n819,987,18\n117,168,530\n805,96,715\n346,949,466\n970,615,88\n941,993,340\n862,61,35\n984,92,344\n425,690,689")

(deftest -main-test
  (is (= 40 (-main test-input 10)) "Part #1")
  (is (= 25272 (-main test-input)) "Part #2"))
