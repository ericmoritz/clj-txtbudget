(ns clj-txtbudget.parser
    (:require [clojure [string :as string]])
    (:require [clojure.java [io :as io]]))


(defn strip-comment [line]
    "Remove comments from a line"
    (string/replace line #"#.+$" ""))


(defn trim-seq [s]
      "trim each item in a sequence"
      (map string/trim s))


(defn str->decimal [s]
      "Convert a string into a BigDecimal"
      (BigDecimal. s))


(defn line->chunks [line]
      "Transfrom a line into processed chunks"
      (-> line
          strip-comment                   ; Strip comments from the line
          (string/split #",")             ; Turn the line into chunks
          trim-seq                        ; Trim the chunk items
      ))


(defn chunks->map [chunk]
      (let [[name amount start-dt recur] chunk]  ; deconstruct the chunk
            {:name name,                            ; map values
             :amount (str->decimal amount),
             :start-dt start-dt,
             :recur recur}))


(defn line->map
     "Transform a line to a map"
     [line]
     (-> line
         line->chunks
         chunks->map))

(defn line-seq->map-seq [lines]
      (->> lines
           (map string/trim)           ; Trim the lines
           (remove #(= (get % 0) \#))  ; Remove any lines starting with #
           (remove string/blank?)      ; Remove any blank lines
           (map line->map)))           ; Map each line to a map


(defn parse-file [filename]
      (-> filename
          (io/make-reader {})  ; Make a reader from the filename
          line-seq             ; Make a line-seq from the reader
          line-seq->map-seq))  ; Turn the line-seq into a map-seq
