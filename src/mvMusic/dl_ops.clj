(ns mvMusic.dl-ops
  (:use (mvMusic global file-ops))
  (:require [clojure.java.io :as io]))

; java method wrappers
(defn file? [x] (.isFile x))
(defn folder? [x] (.isDirectory x))

(defn temp-exists? 
  "Check for existance of temporary folder"
  []
  (.exists (io/as-file (:temp-folder cfg-map))))

(defn create-temp
  "Creates temp folder at location in cfg-map if it does not exists"
  []
 (if-not (temp-exists?)
  (.mkdir (io/as-file (:temp-folder cfg-map)))))

(defn dl-file
  "Returns a file from a user inputted path"
  [user-path]
  (get-file user-path))

(defn list-all
  "takes a file which is a folder and returns a list of all files and folders 
  which are contained within that folder and all subfolters"
  [folder]
  (let [raw-list (apply list (.listFiles folder))]
    (concat raw-list 
            (reduce concat (map list-all (filter folder? raw-list))))))

(defn zip-name 
  "convert returns a relative path from folder (file object) to dl  (file 
  object)"
  [folder dl] 
  (->>
    (.toURI dl)
    (.relativize (.toURI folder))
    (.getPath)))

(defn map-zip-name
  "Takes a list of files objects and a folder file object. It returns a list
  of hash-maps containing the file object and its name to be used in a zip 
  file."
  [file-list folder]
  (map #(hash-map :file %1 :name (zip-name folder %1)) file-list))

(defn new-file
  "takes a file and returns a non-existent file in the temp folder"
  [folder]
  (let [base-name (str (:temp-folder cfg-map) (.getName folder))] 
    (if-not (.exists (io/as-file (str base-name ".zip")))
      (io/as-file (str base-name ".zip"))
      (loop
        [x 1]
        (if-not (.exists (io/as-file (str base-name "_" x ".zip")))
          (io/as-file (str base-name "_" x ".zip"))
          (recur (inc x)))))))

(defn get-zip-list 
  "Takes a folder file object and returns a list containing a map for each file
  and folder contained within the folder and all subdirectories. The map
  contains :file which is a file object for the file and :name which is the
  name which should be used for a zip entry."
  [folder]
  (map-zip-name (list-all folder) folder))

(defn put-z-entry
  "Writes file as a zip entry into zip-file with the name entry-name"
  [file entry-name zip-file]
  (.putNextEntry zip-file (java.util.zip.ZipEntry. entry-name))
  (if (.isFile file) 
    (with-open [input (java.io.FileInputStream. file)]
      (loop 
        [arr (byte-array zip-buff-length)]
        (let [bytes-read (.read input arr)]
          (if (= bytes-read zip-buff-length)
            (do 
              (.write zip-file arr 0 zip-buff-length)
              (recur arr))
            (if (> bytes-read 0)
              (do
                (.write zip-file arr 0 zip-buff-length)
                true)
              true))))))
  (.closeEntry zip-file))

(defn zip-folder
  "Zip given folder into a zip file in temp. Returns file object of new zip
  folder."
  [folder]
  (let [zf (new-file folder)] 
    (with-open [zip-file (java.util.zip.ZipOutputStream. 
                           (java.io.FileOutputStream. zf))]
      (doall
        (map #(put-z-entry (:file %1) (:name %1) zip-file) 
             (get-zip-list folder))))
    zf))
