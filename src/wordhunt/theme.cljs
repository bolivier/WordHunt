(ns wordhunt.theme
  (:require [re-frame.core :as rf]
            [wordhunt.events :as events]
            [wordhunt.subs :as subs]))

(rf/reg-sub
 ::subs/theme
 (fn [db _]
   (first (::theme-list db))))

(rf/reg-event-db
 ::events/toggle-theme
 (fn [db _]
   (update
    db
    ::theme-list
    #(drop 1 %))))
