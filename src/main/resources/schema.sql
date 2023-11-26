CREATE TABLE y.Users (
                         id BIGINT PRIMARY KEY IDENTITY,
                         username VARCHAR(50) UNIQUE,
                         email VARCHAR(100) UNIQUE,
                         password VARCHAR(100),
                         fullName VARCHAR(100),
                         bio TEXT,
                         location VARCHAR(100),
                         website VARCHAR(100),
                         profilePictureUrl VARCHAR(255)
);

CREATE TABLE y.Yaps (
                        YapID INT PRIMARY KEY IDENTITY,
                        UserID INT NOT NULL,
                        Content VARCHAR(280) NOT NULL,
                        Timestamp DATETIME NOT NULL,
                        RepostCount INT DEFAULT 0,
                        LikeCount INT DEFAULT 0,
                        ReplyCount INT DEFAULT 0,
                        FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE y.Followers (
                           FollowerID INT NOT NULL,
                           FollowingID INT NOT NULL,
                           PRIMARY KEY (FollowerID, FollowingID),
                           FOREIGN KEY (FollowerID) REFERENCES Users(UserID),
                           FOREIGN KEY (FollowingID) REFERENCES Users(UserID)
);

CREATE TABLE y.Replies (
                         ReplyID BIGINT PRIMARY KEY IDENTITY,
                         YapID INT NOT NULL,
                         UserID INT NOT NULL,
                         Content VARCHAR(280) NOT NULL,
                         Timestamp DATETIME NOT NULL,
                         FOREIGN KEY (YapID) REFERENCES Yaps(YapID),
                         FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE y.Reposts (
                          RepostID BIGINT PRIMARY KEY IDENTITY,
                          YapID INT NOT NULL,
                          UserID INT NOT NULL,
                          Timestamp DATETIME NOT NULL,
                          FOREIGN KEY (YapID) REFERENCES Yaps(YapID),
                          FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE y.Likes (
                       LikeID BIGINT PRIMARY KEY IDENTITY   ,
                       YapID INT NOT NULL,
                       UserID INT NOT NULL,
                       FOREIGN KEY (YapID) REFERENCES Yaps(YapID),
                       FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE y.DirectMessages (
                                MessageID BIGINT PRIMARY KEY IDENTITY,
                                SenderID INT NOT NULL,
                                ReceiverID INT NOT NULL,
                                Message TEXT NOT NULL,
                                Timestamp DATETIME NOT NULL,
                                FOREIGN KEY (SenderID) REFERENCES Users(UserID),
                                FOREIGN KEY (ReceiverID) REFERENCES Users(UserID)
);

CREATE TABLE y.Hashtags (
                          HashtagID BIGINT PRIMARY KEY IDENTITY,
                          HashtagText VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE y.YapHashtags (
                          YapID INT NOT NULL,
                          HashtagID INT NOT NULL,
                          FOREIGN KEY (YapID) REFERENCES Yaps(YapID),
                          FOREIGN KEY (HashtagID) REFERENCES Hashtags(HashtagID)
);