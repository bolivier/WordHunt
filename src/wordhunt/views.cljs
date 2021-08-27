(ns wordhunt.views
  (:require
   [re-frame.core :as rf]
   [wordhunt.subs :as subs]
   ))

(defn main-panel []
  (let [name (rf/subscribe [::subs/name])]
    [:div
     [:h1
      "Hello from " @name]
     ]))
