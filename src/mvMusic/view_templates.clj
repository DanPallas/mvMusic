(ns mvMusic.view-templates
  (:use [hiccup core page]
        [mvMusic.file-ops]))

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
  [:head
    (wrap-tag title :title)
    [:link {:rel "stylesheet" :type "text/css" :href "/main.css"}] ])

(defn format-directories
  [path]
  (->> (directory-url-list path)
       (map #(wrap-tag (first %1) :a {:href (second %1)}))
       (map #(wrap-tag %1 :div {:class "directories"}))))

(defn format-files 
  "format files into table rows"
  [path]
  (->> (file-url-list path)
       (map #(wrap-tag (first %1) :a {:href (second %1)}))
       (map #(wrap-tag %1 :div {:class "files"}))))

(defn body 
  "Generate body html"
  [path]
  [:body
   [:div {:id "parent"}
    [:h1 "mvMusic"] 
    (vec (concat [:div {:id "browse-list"}] 
          (format-directories path)
           (format-files path)))]])
