(ns wordhunt.language
  (:require [re-frame.core :as rf]
            [wordhunt.subs :as subs]
            [wordhunt.events :as events]))

(rf/reg-sub
 ::subs/language
 (fn [db _]
   (::language db)))

(rf/reg-event-db
 ::events/set-language
 (fn [db [_ language]]
   (assoc db
          ::language language
          ::word-query "")))

(rf/reg-sub
 ::subs/word-query
 (fn [db _]
   (::word-query db)))

(rf/reg-event-db
 ::events/word-query-change
 (fn [db [_ value]]
   (assoc db ::word-query value)))
