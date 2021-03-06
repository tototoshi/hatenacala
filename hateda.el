;; Copyright (c) 2011 Toshiyuki Takahashi.
;; All rights reserved.
;;
;; Redistribution and use in source and binary forms, with or without
;; modification, are permitted provided that the following conditions
;; are met:
;;
;;  1. Redistributions of source code must retain the above copyright
;;     notice, this list of conditions and the following disclaimer.
;;  2. Redistributions in binary form must reproduce the above copyright
;;     notice, this list of conditions and the following disclaimer in
;;     the documentation and/or other materials provided with the
;;     distribution.
;;  3. The name of the author may not be used to endorse or promote
;;     products derived from this software without specific prior
;;     written permission.
;;
;;  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS
;;  OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
;;  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
;;  ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
;;  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
;;  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
;;  GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
;;  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
;;  IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
;;  OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
;;  IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

(require 'cl)
(require 'helm)
(require 'markdown-mode)

(defvar hateda-executable "~/bin/hateda")
(defvar hateda-draft-dir "~/Documents/hateda/")
(defvar hateda-draft-entry-regexp "Draft id: \\([1-9][0-9]*\\)  title: \\(.*\\)  date: \\([0-9]+\\)")
(defstruct hateda-draft-entry
  id  title  date)

(defvar hateda-draft-tmp-file "/tmp/hateda-draft")

(defun hateda-char-replace (from-char to-char string)
  (let ((chars (string-to-list string)))
    (apply #'string
           (loop for c in chars
                 collect (if (= from-char c) to-char c)))))

(defun hateda-delete-trailing-slash (string)
   (cond ((= ?/ (first (reverse (string-to-list string))))
          (substring string 0 (1- (length string))))
         (t string)))

(defun hateda-basename (abspath)
  (first (reverse (split-string (hateda-delete-trailing-slash abspath) "/"))))

(defun hateda-draft-add-this-buffer ()
  (interactive)
  (write-region (point-min) (point-max) hateda-draft-tmp-file nil nil)
  (message "Wait a moment.....")
  (shell-command (hateda-mkString `(,hateda-executable "draft" "add" ,hateda-draft-tmp-file) " ")))

(defun hateda-mkString (lst &optional spliter)
  (reduce (lambda (m o) (concat m (or spliter "") o)) lst))

(defun hateda-lines-to-list (lines)
  (split-string lines "\n"))

(defun hateda-draft-list ()
  (shell-command-to-string (hateda-mkString `(,hateda-executable "draft" "list") " ")))

(defun hateda-get-id-of-current-buffer ()
  (let ((filename (hateda-basename (buffer-file-name))))
    (cond ((string-match "\\([0-9]+\\).*" filename)
           (match-string 1 filename))
          (t ""))))

(defun hateda-draft-update ()
  (interactive)
  (write-region (point-min) (point-max) hateda-draft-tmp-file nil nil)
  (message "Wait a moment.....")
  (shell-command
   (hateda-mkString
    `(,hateda-executable "draft" "update" ,(hateda-get-id-of-current-buffer) ,hateda-draft-tmp-file)
    " ")))

(defun hateda-string-to-draft-entry (string)
  (when (string-match hateda-draft-entry-regexp string)
    (make-hateda-draft-entry :id (match-string 1 string)
                             :title (match-string 2 string)
                             :date (match-string 3 string))))

(defun hateda-draft-make-file-name (id title)
  (concat
   (hateda-delete-trailing-slash hateda-draft-dir)
   "/" id "-" (hateda-char-replace ?/ ?_ title)))

(defun hateda-draft-open-file (id title)
  (unless (file-exists-p hateda-draft-dir)
    (mkdir hateda-draft-dir))
  (with-current-buffer (find-file (hateda-draft-make-file-name id title))
    (erase-buffer)
    (shell-command (hateda-mkString `(,hateda-executable "draft" "get" ,id) " ") (current-buffer))
    (hateda-mode)))

(defun helm-hateda-draft-open-file (description)
  (message "Wait a moment....")
  (let ((entry (hateda-string-to-draft-entry description)))
    (hateda-draft-open-file (hateda-draft-entry-id entry) (hateda-draft-entry-title entry))))

(defun helm-hateda-draft-rm (description)
  (message "Wait a moment....")
  (let ((entry (hateda-string-to-draft-entry description)))
    (hateda-draft-rm (hateda-draft-entry-id entry))))

(defun hateda-draft-rm (id)
  (shell-command (hateda-mkString `(,hateda-executable "draft" "rm" ,id) " ")))

(defun helm-hateda-draft-entries-get-candidates ()
  (message "Wait a moment....")
  (hateda-lines-to-list (hateda-draft-list)))

(define-helm-type-attribute 'hateda
  '((action ("Open" . helm-hateda-draft-open-file)
            ("Delete" . helm-hateda-draft-rm))))

(setq helm-c-source-hateda-draft-entries
  '((name . "hatena-diary")
    (candidates . helm-hateda-draft-entries-get-candidates)
    (requires-pattern . 0)
    (candidate-number-limit . 50)
    (type . hateda)))

(defun helm-hateda ()
  (interactive)
  (helm 'helm-c-source-hateda-draft-entries))

(define-generic-mode hateda-mode
  nil nil
  '(("^\\*[^*].*$" . markdown-header-face-1)
    ("^\\*\\*[^*].*$" . markdown-header-face-2)
    ("^\\*\\*\\*.*$" . markdown-header-face-3)
    ("\\[.*:title.*\\]" . markdown-url-face)
    ("https?://[^\t\n ]+" . markdown-url-face)
    (">>\\(?:.\\|\n\\)*?<<" . markdown-blockquote-face)
    (">||\\(?:.\\|\n\\)*?||<" . markdown-pre-face)
    (">|[^|]+|\\(?:.\\|\n\\)*?||<" . markdown-pre-face))
  nil
  (list
   (lambda ()
     (set (make-local-variable 'font-lock-multiline) t)
     (setq major-mode 'hateda-mode
           mode-name "Hateda")
     (setq hateda-mode-local-map (make-keymap))
     (define-key hateda-mode-local-map (kbd "C-c C-c") 'hateda-draft-update)
     (define-key hateda-mode-local-map (kbd "C-c C-n") 'hateda-draft-add-this-buffer)
     (define-key hateda-mode-local-map (kbd "C-c C-r") 'font-lock-fontify-buffer)
     (use-local-map hateda-mode-local-map)
     )))

(provide 'hateda)

