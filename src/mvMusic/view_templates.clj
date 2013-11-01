(ns mvMusic.view-templates
  (:use [hiccup core page]
        [mvMusic.file-ops]
        [mvMusic.configuration]))

(defn wrap-tag
  "Returns a hiccup vector with the given content tag and optional attribute
  map"
  [content tag & [attribute-map]]
  (vec (filter #(not (nil? %1)) (list tag attribute-map content))))

(defn head
  "Generate hiccup html for header
    params:
      title: string to use as title"
  [title]
  (wrap-tag (wrap-tag title :title) :head))

(defn format-directories
  [path]
  (->> (directory-url-list path)
       (map #(wrap-tag (first %1) :a {:href (second %1)}))
       (map #(wrap-tag %1 :td))
       (map #(wrap-tag %1 :tr))))

(defn format-files 
  "format files into table rows"
  [path]
  (->> (file-url-list path)
       (map #(wrap-tag (first %1) :a {:href (second %1)}))
       (map #(wrap-tag %1 :td))
       (map #(wrap-tag %1 :tr))))

(defn body 
  "Generate body html"
  [path]
  [:body
   [:h1 "mvMusic"] 
   (vec (concat [:table {:border 1}] 
          (format-directories path)
           (format-files path)))])


