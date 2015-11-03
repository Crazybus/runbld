(ns runbld.publish.elasticsearch
  (:require [elasticsearch.document :as doc]
            [runbld.util.date :as date]))

(defn expand-index-name [s]
  (if (.contains s "'")
    (date/expand s)
    s))

(defn make-doc [opts]
  (merge
   (dissoc (get opts :proc) :proc)
   {
    ;; don't use this for :_id so we can take advantage of flake
    :id (get-in opts [:id])}))

(defn prepare-opts [opts]
  (assoc opts
         :es {:index (expand-index-name (:es.index.build opts))
              :type "b"
              :body (make-doc opts)}))

(defn index [opts]
  (doc/index (:es.conn opts) (:es opts)))

