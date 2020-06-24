(ns posh.reagent
  (:require #?(:clj  [posh.plugin-base :as base]
               :cljs [posh.plugin-base :as base :include-macros true])
            [datascript.core :as d]
            [reagent.core :as r]
            [reagent.ratom :as ra]))

(defn derive-reaction [reactions key f & local-mixin]
  #?(:clj nil
     :cljs
     (apply ra/make-reaction
       #(apply f (mapv deref reactions))
       local-mixin)))

(def dcfg
  (let [dcfg {:db            d/db
              :pull*         d/pull
              :pull-many     d/pull-many
              :q             d/q
              :filter        d/filter
              :with          d/with
              :entid         d/entid
              :transact!     d/transact!
              :listen!       d/listen!
              :conn?         d/conn?
              :react         deref
              :ratom         #?(:clj  nil
                                :cljs r/atom)
              :make-reaction #?(:clj  nil
                                :cljs ra/make-reaction)
              :derive-reaction derive-reaction}]
    (assoc dcfg :pull (partial base/safe-pull dcfg))))

(base/add-plugin dcfg)
