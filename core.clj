;; Daniel Ewing Final Project
;; sources which were used
;; https://kimh.github.io/clojure-by-example/#symbol
;; https://clojure.org/api/cheatsheet  for apply functionality.
(ns untitled.core)

;; slurp the file as a integer converted from strings.
(defn read-file [file]
  (map read-string
       (clojure.string/split-lines
         (slurp file))
       )
)

(defn split-list-in-two [list-to-split]
  (split-at (/ (count list-to-split) 2) list-to-split))


;; merge-sort
(defn merge-sort [left right]
  (loop [merged-data [] left-data-to-sort left right-data-to-sort right]                                          ;; loop through the data, adding sorted items to the new merged-data list
    (if (and (seq left-data-to-sort) (seq right-data-to-sort))                                                    ;; check for empty lists
      (if (< (first left-data-to-sort) (first right-data-to-sort))                                                ;; if the heads of the left and right hand side are in ascending order
        (recur (conj merged-data (first left-data-to-sort)) (rest left-data-to-sort) right-data-to-sort)          ;;recursive call to append items to the list if condition is true
        (recur (conj merged-data (first right-data-to-sort)) left-data-to-sort (rest right-data-to-sort))         ;;recursive call to append items to the list if condition is false
        )
      (concat merged-data left-data-to-sort right-data-to-sort)                                                   ;; one of the lists which requires sorting is empty, concatenate the rest, which has already been sorted.
      )
    )
)


;;sorts the list it is given on a single thread.
(defn one-thread-merge-sort [list-to-sort]
  (if (< (count list-to-sort) 2) list-to-sort                                                       ;;if more than two items exist in the list which needs sorted
                                (apply merge-sort                                                   ;;call merge-sort function
                                     (map one-thread-merge-sort (split-list-in-two list-to-sort)           ;;recursive call mapped to a single thread, these two lists will be sorted when they're returned
                                          )
                                     )
                                 )
)


;;splits list into two parts and sorts each on a different thread.
(defn two-thread-merge-sort [list-to-sort]
  (apply merge-sort
         (pmap one-thread-merge-sort (split-list-in-two list-to-sort)                                ;;call the function and map it to multiple threads passing the split list as parameters.
               )
         )
)

;;splits list into two parts, and calls the previous function to split them again.y.
(defn four-thread-merge-sort [list-to-sort]
  (apply merge-sort
         (pmap two-thread-merge-sort  (split-list-in-two list-to-sort)
               )
         )
)

;;splits list into two parts, and calls the previous function to split them again.
(defn eight-thread-merge-sort [list-to-sort]
  (apply merge-sort
         (pmap four-thread-merge-sort (split-list-in-two list-to-sort)
               )
         )
)

;;splits list into two parts, and calls the previous function to split them again.
(defn sixteen-thread-merge-sort [list-to-sort]
  (apply merge-sort
         (pmap eight-thread-merge-sort  (split-list-in-two list-to-sort)
               )
         )
  )

;;splits list into two parts, and calls the previous function to split them again.
(defn thirty-two-thread-merge-sort [list-to-sort]
  (apply merge-sort
         (pmap sixteen-thread-merge-sort  (split-list-in-two list-to-sort)
               )
         )
  )


;; simple test function, does what it says this ensures all sorting functions are properly working since they recursively call each-other until each equal chunk has been sorted, and those chunks are resorted.
(defn merge-sort-and-print [data-structure-to-sort]
  (println (thirty-two-thread-merge-sort data-structure-to-sort))
)
;; end of function definitions.\


;; our list of integers we will sort, read from "numbers.txt"
(def my-list (read-file "numbers.txt"))
;; end of variable definitions.



(print "Proof of Ascending Sorting Functionality for a list: ")
(merge-sort-and-print '(5 1 2 -3 2 5 6 1 -2 -3 -12 612 -23))
(print "Proof of Ascending Sorting Functionality for a vector: ")
(merge-sort-and-print [5 1 2 -3 2 5 6 1 -2 -3 -12 612 -23])
(print "Time to populate the list: ")  ;; used to determine sort time without the inclusion of file access time, will be subtracted in report.
(time (read-file "numbers.txt"))
(print "Rough amount of time to populate the list after having done so already: ")  ;; used to determine sort time without the inclusion of file access time, will be subtracted in report.
(time (read-file "numbers.txt"))
(print "1 Thread: ")
(time (one-thread-merge-sort my-list))
(print "2 Threads: ")
(time (two-thread-merge-sort my-list))
(print "4 Threads: ")
(time (four-thread-merge-sort my-list))
(print "8 Threads: ")
(time (eight-thread-merge-sort my-list))
(print "16 Threads: ")
(time (sixteen-thread-merge-sort my-list))
(print "32 Threads: ")
(time (thirty-two-thread-merge-sort my-list))
(print "Clojure Library Sort: ")
(time (sort my-list))