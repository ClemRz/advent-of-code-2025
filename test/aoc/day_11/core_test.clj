(ns aoc.day-11.core-test
  (:require [aoc.day-11.core :refer :all]
            [clojure.test :refer :all]))

(def test-input-1 "aaa: you hhh\nyou: bbb ccc\nbbb: ddd eee\nccc: ddd eee fff\nddd: ggg\neee: out\nfff: out\nggg: out\nhhh: ccc fff iii\niii: out")
(def test-input-2 "svr: aaa bbb\naaa: fft\nfft: ccc\nbbb: tty\ntty: ccc\nccc: ddd eee\nddd: hub\nhub: fff\neee: dac\ndac: fff\nfff: ggg hhh\nggg: out\nhhh: out")

(deftest -main-test
  (is (= 5 (-main test-input-1 you)) "Part #1")
  (is (= 2 (-main test-input-2 svr)) "Part #2"))
