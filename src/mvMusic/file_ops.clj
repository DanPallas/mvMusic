(ns mvMusic.file-ops
  (:use [mvMusic.configuration])
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def path-delimiter "<")
(def space-replacement " ")
(def dot-replacement ">")

(defn get-name [x]
  (.getName x))
(defn get-path [x]
  (.getPath x))

(defn pair-name-path [x]
  [(get-name x) (get-path x)])

(defn list-directories 
  "return sorted a vector containing the filename and path of each direct 
  non-hidden child directory of file"
  [file]
  (vec (->> (apply vector (.listFiles file))
            (filter #(and (not (.isHidden %1)) (.isDirectory %1)))
            (map pair-name-path))))

(defn list-files 
  "return a vector containing the filename and path of each non-hidden file
  which is a direct child of the file"
  [file]
  (vec (->> (apply vector (.listFiles file))
            (filter #(and (not (.isHidden %1)) (.isFile %1)))
            (map pair-name-path))))

(defn to-relative 
  "convert path to path relative to music folder"
  [path] 
  (->>
    (.toURI (java.io.File. path))
    (.relativize (.toURI (java.io.File. (:music-folder cfg-map))))
    (.getPath)))

(defn remove-illegal 
  "removes both forward and backward slashes and spaces from paths and replaces 
  them with path delimiter"
  [path]
  (-> (string/replace path "/" path-delimiter)
      (string/replace  "\\" path-delimiter)
      (string/replace  " " space-replacement)
      (string/replace  "." dot-replacement)))

(defn file-list 
  "Get list of direct children from path using function list-func which has had 
  it's path made relative to music-folder and has had it's slashes removed"
  [path list-func]
  (->> (list-func path)
       (map #(vector (first %1) (to-relative (second %1))))
       (map #(vector (first %1) (remove-illegal (second %1))))
       (vec)))

(defn replace-url-chars
  [path]
  "Converts url path characters back to file path characters."
  (-> (string/replace path path-delimiter "/")
      (string/replace space-replacement " ")
      (string/replace dot-replacement ".")))

(defn sanitize
  "Tests for attempted unauthorized access. Returns empty string if unauthorized
  access detect. Otherwise returns the path." 
  [path]
  (if 
    (not= nil (re-find 
                #"^\.\.[^a-z\ 0-9.]|[^a-z\ 0-9.]\.\.[^a-z\ 0-9.]|[^a-z\ 0-9.]\.\.$|^..$" 
                path))
    "/"
    path))

(defn clean
  [path]
  "Takes url path parameter and returns safe, path parameter with url characters 
  replaced"
  (->> (sanitize (replace-url-chars path))
       (java.io.File. (io/as-file (:music-folder cfg-map)))))
