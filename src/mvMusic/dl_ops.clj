(ns mvMusic.dl-ops
  (:use [mvMusic.global]
        [mvMusic.file-ops])
  (:require [clojure.java.io :as io]))

(defn temp-exists? 
  "Check for existance of temporary folder"
  []
  (.exists (io/as-file (:temp-folder cfg-map))))

(defn create-temp
  "Creates temp folder at location in cfg-map if it does not exists"
  []
 (if-not (temp-exists?)
  (println (.mkdir (io/as-file (:temp-folder cfg-map))))))

(defn dl-file
  "Returns a file from a user inputted path"
  [user-path]
  (get-file user-path))

