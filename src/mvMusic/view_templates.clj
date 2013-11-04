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
  [path]
  "format files into table rows"
  (->> (file-url-list path)
       (map #(vector :div {:class "files"}
                     [:div {:class "filename"} (first %1)]
                     [:div {:class "download"} 
                      [:a {:href (second %1)} "Download"]]))))

(defn body 
  "Generate body html from a java file which should be a folder"
  [folder]
  [:body
   [:div {:id "parent"}
    [:h1 "mvMusic"] 
    (vec (concat [:div {:id "browse-list"}] 
          (format-directories folder)
           (format-files folder)))]])
