(ns wordhunt.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as rf]
   [wordhunt.events :as events]
   [wordhunt.views :as views]
   [wordhunt.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (rf/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
