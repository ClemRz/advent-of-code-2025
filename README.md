# My attempt to solve the 2025 AoC with Clojure

[Advent of code 2025](https://adventofcode.com/2025/) - [Clojure](https://clojure.org/)

```
          *
         /.\
        /..'\
        /'.'\
       /.''.'\
       /.'.'.\
"'""""/'.''.'.\""'"'"
      ^^^[_]^^^
```
## Performance

This year my personal goal is to solve each part under one second of runtime.

| Day | Part 1 | Part 2 | Comments                                |
|-----|--------|--------|-----------------------------------------|
| 1   | 25 ms ✓ | 4 ms ✓ |                                         |
| 2   | 1498 ms | 5509 ms | Can be largely optimized by usign regex |
| 3   | 37 ms ✓ | 78 ms ✓ |                                         |
| 4   | 129 ms ✓ | 595 ms ✓ |                                         |
| 5   | 59 ms ✓ | 1 ms ✓ |                                         |
| 6   | 33 ms ✓ | 24 ms ✓ |                                         |
| 7   | 44 ms ✓ | 179 ms ✓ |                                         |
| 8   | 557 ms ✓ | 758 ms ✓ |                                         |
| 9   | 172 ms ✓ | 9318 ms | IDK how to make part 2 faster            |

