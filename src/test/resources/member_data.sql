DELETE FROM member;
ALTER TABLE member auto_increment = 1;
INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');
