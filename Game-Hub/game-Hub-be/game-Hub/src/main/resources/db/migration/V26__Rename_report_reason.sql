DELETE
FROM game_hub.report;

ALTER TABLE game_hub.report_reason
  RENAME COLUMN reason TO community_guideline;

ALTER TABLE game_hub.report_reason RENAME to community_guidelines;

INSERT INTO game_hub.community_guidelines (community_guideline, description)
VALUES ('Inappropriate behavior / language', 'Offensive, disrespectful, or abusive behavior was detected.'),
       ('Harassment / Bullying', 'A user was targeted with repeated harassment, bullying, or intimidation.'),
       ('Hate speech / Discrimination',
        'Content promotes hatred, discrimination, or hostility toward a person or group.'),
       ('Threats / Violence', 'Threats of violence or encouragement of violent behavior were detected.'),
       ('Inappropriate content', 'Content shared is offensive, violent, or explicit.'),
       ('Sexual content / Nudity', 'Sexually explicit content or inappropriate nudity was shared.'),
       ('Spam / Advertising', 'Unwanted messages or promotional content were sent.'),
       ('Fake account / scam', 'Account appears fake or involved in misleading activity.'),
       ('Fraud / Deception', 'The user appears to be involved in fraudulent or intentionally deceptive activity.'),
       ('Malicious links / Malware',
        'Suspicious links, malicious software, or potentially harmful content was shared.'),
       ('Privacy violation', 'Personal information was shared without consent.'),
       ('Impersonation / Identity theft', 'A user appears to be impersonating another person or using their identity.'),
       ('Cheating / Exploitation', 'The user appears to be exploiting game systems, bugs, or other users unfairly.'),
       ('Illegal activity', 'Content or behavior appears to involve illegal activity.'),
       ('Other', 'The report does not match any of the available categories.');