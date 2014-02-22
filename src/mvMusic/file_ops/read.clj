(ns mvMusic.file-ops.read
  (:use (mvMusic global))
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

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
  (->> (.listFiles file)
            (filter #(and (not (.isHidden %1)) (.isDirectory %1)))
            (map pair-name-path)))

(defn list-files 
  "return a vector containing the filename and path of each non-hidden file
  which is a direct child of the file"
  [file]
  (->> (.listFiles file)
       (filter #(and (not (.isHidden %1)) (.isFile %1)))
       (map pair-name-path)))

(defn to-relative 
  "convert path to path relative to music folder"
  [path] 
  (->>
    (.toURI (java.io.File. path))
    (.relativize (.toURI (java.io.File. (:music-folder cfg-map))))
    (.getPath)))

(defn file-list 
  "Get list of direct children from path using function list-func which has had 
  it's path made relative to music-folder and has had it's slashes removed"
  [path list-func]
  (->> (list-func path)
       (map #(vector (first %1) (to-relative (second %1))))))

(defn directory-url-list 
  "Returns a list containing a vector for each non-hidden child directory of 
  of the passed path Each vector contains the filename and a url 
  representation of the file."
  [folder]
  (->> (list-directories folder)
       (map #(vector (first %1) (->> (to-relative (second %1))
                                     (str browse-path)))) ))

(defn file-url-list 
  "Returns a vector containing a vector for each non-hidden child file of 
  of the passed directory. Each vector contains the 
  filename and a url representation of the file path."
  [path]
  (->> (list-files path)
       (map #(vector (first %1) (->> (to-relative (second %1)) 
                                     (str download-path))))))

(defn sanitize
  "Tests for attempted unauthorized access. Returns empty string if unauthorized
  access detect. Otherwise returns the path." 
  [path]
  (if 
    (not= nil (re-find 
        #"^\.\.[^a-z\ 0-9.]|[^a-z\ 0-9.]\.\.[^a-z\ 0-9.]|[^a-z\ 0-9.]\.\.$|^..$" 
                path))  "/" path))

(defn get-file
  "Takes user inputted path parameter and returns a file in the music folder"
  [user-path]
  (->> (sanitize user-path)
       (java.io.File. (io/as-file (:music-folder cfg-map)))))

(defn list-all
  "takes a file which is a folder and returns a list of all music files and 
  folders which are contained within that folder and all subfolters"
  [folder]
  (let [raw-list (apply list (.listFiles folder))]
    (concat (filter #(or 
                       (.isDirectory %) 
                       (re-matches supported-files (.getName %))) 
                    raw-list) 
            (reduce concat (map list-all (filter #(.isDirectory %) raw-list))))))

