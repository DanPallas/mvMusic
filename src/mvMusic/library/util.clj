(ns mvMusic.library.util)


(defn albums=?
  "Multiple arity equals function to determine if albums are equivalent It 
  ignores all values except for title, artist, and sort. Returns true or false."
  [x y & r]
  (as->
    (conj (or r ()) x y) $
    (map #(vector (:title %) (:artist %) (:sort %)) $)
    (apply = $)))

(defn extract-albums
  "Takes  either a collection of maps representing songs or a map of a single 
  song, and returns a list of maps representing albums"
  [x]
  (let
    [songs (if (map? x) (vector x) x)
     albums (vec (map 
                   #(assoc {} :title (:album-title %) 
                              :artist (:album-artist %) 
                              :image (:image %) 
                              :sort (:album-sort %)) 
                   songs))] 
    (remove nil?
            (for [i (range (count albums))]
              (if 
                (some 
                  (partial albums=? (nth albums i))
                  (subvec albums (inc i)))
                nil
                (nth albums i))))))

(defn prep-songs
  "takes a raw-song or collection of raw-songs songs and a collection of albums from the library, and returns songs containing just the fields kept in the library
 and the correct album relationship"
  [songs albums]
  (let 
    [songs (if (map? songs) (vector songs) songs)
     ]
    (-> (map #(assoc % :album-id (get-in albums [[(:album-title %) 
                                                  (:album-artist %) 
                                                  (:album-sort %)] 
                                                 :id]))
             songs)
        (select-keys [:track :disc :title :artist :length :year :album-id
                      :extension :genre :folder :filename :path :mod-date]))))
