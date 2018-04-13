(ns clojureserv.core
  (:import (java.net Socket)
           (java.io PrintWriter)
           (java.util Scanner)))

(def tcp-socket (ref Socket.))

(defn connect [address]
  (dosync
    (.connect tcp-socket address)))

(defn send-string [str]
  (dosync
    (->
      (deref tcp-socket)
      .getOutputStream
      PrintWriter.
      (.println str))))

(defn receive-string []
  (dosync
    (->
      (deref tcp-socket)
      .getInputStream
      Scanner.
      .next)))