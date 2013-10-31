(ns mvMusic.view-templates
  (:use [hiccup core page]
        [mvMusic.file-ops]
        [mvMusic.configuration]))

(defn head
  "Generate hiccup html for header
    params:
      title: string to use as title"
  [title]
  [:head
   [:title title]])

(defn format-directories 
  "format directories into table rows"
  [path]
  (->> (file-list path list-directories)
       (map #(conj [:a {:href (second %1)}] (first %1)))
       (vec)
       (map #(conj [:td] %1))
       (map #(conj [:tr] %1))))

(defn format-files 
  "format files into table rows"
  [path]
  (->> (file-list path list-files)
       (map #(conj [:a {:href (second %1)}] (first %1)))
       (vec)
       (map #(conj [:td] %1))
       (map #(conj [:tr] %1))))

(defn body 
  "Generate body html"
  [path]
  [:body
   [:h1 "mvMusic"] 
   (vec (concat [:table {:border 1}] 
          (format-directories path)
           (format-files path)))])
