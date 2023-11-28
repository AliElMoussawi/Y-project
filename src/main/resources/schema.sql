CREATE TABLE y.Users (
    UserID INT PRIMARY KEY IDENTITY,
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
    FOREIGN KEY (UserID) REFERENCES y.Users(UserID)
);

CREATE TABLE y.Followers (
    FollowerID INT NOT NULL,
    FollowingID INT NOT NULL,
    PRIMARY KEY (FollowerID, FollowingID),
    FOREIGN KEY (FollowerID) REFERENCES y.Users(UserID),
    FOREIGN KEY (FollowingID) REFERENCES y.Users(UserID)
);

CREATE TABLE y.Replies (
    ReplyID INT PRIMARY KEY IDENTITY,
    YapID INT NOT NULL,
    UserID INT NOT NULL,
    Content VARCHAR(280) NOT NULL,
    Timestamp DATETIME NOT NULL,
    FOREIGN KEY (YapID) REFERENCES y.Yaps(YapID),
    FOREIGN KEY (UserID) REFERENCES y.Users(UserID)
);

CREATE TABLE y.Reposts (
    RepostID INT PRIMARY KEY IDENTITY,
    YapID INT NOT NULL,
    UserID INT NOT NULL,
    Timestamp DATETIME NOT NULL,
    FOREIGN KEY (YapID) REFERENCES y.Yaps(YapID),
    FOREIGN KEY (UserID) REFERENCES y.Users(UserID)
);

CREATE TABLE y.Likes (
    LikeID INT PRIMARY KEY IDENTITY   ,
    YapID INT NOT NULL,
    UserID INT NOT NULL,
    FOREIGN KEY (YapID) REFERENCES y.Yaps(YapID),
    FOREIGN KEY (UserID) REFERENCES y.Users(UserID)
);

CREATE TABLE y.DirectMessages (
	MessageID INT PRIMARY KEY IDENTITY,
	SenderID INT NOT NULL,
	ReceiverID INT NOT NULL,
	Message TEXT NOT NULL,
	Timestamp DATETIME NOT NULL,
	FOREIGN KEY (SenderID) REFERENCES y.Users(UserID),
	FOREIGN KEY (ReceiverID) REFERENCES y.Users(UserID)
);

CREATE TABLE y.Hashtags (
  HashtagID INT PRIMARY KEY IDENTITY,
  HashtagText VARCHAR(50) NOT NULL UNIQUE
);

