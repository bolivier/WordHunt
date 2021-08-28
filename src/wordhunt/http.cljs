(ns wordhunt.http
  (:require [cljs.core.async :refer [chan put!]]))

(defn GET [url]
  (let [c (chan)]
    (js/fetch url)
    c))
