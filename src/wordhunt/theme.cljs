(ns wordhunt.theme
  (:require [re-frame.core :as rf]
            [wordhunt.events :as events]
            [wordhunt.subs :as subs]))

(rf/reg-sub
 ::subs/theme
 (fn [db _]
   (::theme db)))

(rf/reg-event-db
 ::events/set-theme
 (fn [db [_ theme]]
   (assoc db ::theme theme)))
