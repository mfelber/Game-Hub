INSERT INTO game_hub.report_status (status)
VALUES ('NEW'),
       ('IN REVIEW'),
       ('ACCEPTED'),
       ('REJECTED');

INSERT INTO game_hub.report_reason (reason, description)
VALUES ('Inappropriate behavior / language', 'Offensive, disrespectful, or abusive behavior was detected.'),
       ('Spam / Advertising', 'Unwanted messages or promotional content were sent.'),
       ('Fake account / scam', 'Account appears fake or involved in misleading activity.'),
       ('Inappropriate content', 'Content shared is offensive, violent, or explicit.'),
       ('Privacy violation', 'Personal information was shared without consent.'),
       ('Other','')