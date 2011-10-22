(ns clj-txtbudget.parser
    (:require [clojure [string :as string]])
    (:require [clojure.java [io :as io]]))


(defn strip-comment
    "Remove comments from a line"
    [line]
    (string/replace line #"#.+$" ""))


(defn trim-seq
      "trim each item in a sequence"
      [s]
      (map string/trim s))


(defn line->chunks
      "Transfrom a line into processed chunks"
      [line]
      (-> line
          strip-comment        ; Remove comments from the line
          (string/split #",")  ; Turn the line into chunks
          trim-seq))           ; Trim the chunk items


(defn chunks->map [chunk]
      (let [[name amount-str start-dt recur] chunk  ; deconstruct the chunk
            amount (BigDecimal. amount-str)]        ; cast amount to BigDecimal
            {:name name,                            ; map values
             :amount amount,
             :start-dt start-dt,
             :recur recur}))


(defn line->map
     "Transform a line to a map"
     [line]
     (-> line
         line->chunks
         chunks->map))

(defn line-seq->map [lines]
      (->> lines
           (map string/trim)           ; Trim the lines
           (remove #(= (get % 0) \#))  ; Remove any lines starting with #
           (remove string/blank?)      ; Remove any blank lines
           (map line->map)))           ; Map each line to a map


(defn parse-file [filename]
      (-> filename
          (io/make-reader {})
          line-seq
          line-seq->map))
