/* -----------------------------------------------------------------------------
   TEST DATA - FAKE

   Script intended to simulate a possible porting of pre-existing data.
   -----------------------------------------------------------------------------
   Please note:
   This script inserts sample data (names, titles, texts, e-mail addresses) 
   intended exclusively for testing, demo, or development purposes.  
   There is no real data and no reference to things or people is intended. 
   All values ​​are entirely fictional and reported for development purposes only. 
   I repeat, they refer to nothing or anyone. 
   Additionally, they should never be used in a production environment.
   ----------------------------------------------------------------------------- */

-- 12 Articles

INSERT INTO articles (title, subject, content, published_date) VALUES
  ('Cardiac Surgery Outcomes Overview',
   'Cardiac Surgery',
   'Overview of 30-day mortality and recovery metrics in recent trials',
   DATE '2026-01-15'),
  ('Telemedicine in Rural Clinics',
   'Telemedicine',
   'Evaluation of video-consultation platforms in remote health settings',
   DATE '2026-02-20'),
  ('Robotic Orthopedic Reconstruction',
   'Orthopedics',
   'Assessing accuracy and cost-effectiveness of robotic assistance',
   DATE '2026-03-10'),
  ('Post-Surgery Nutrition Protocols',
   'Nutrition',
   'Impact on wound healing and readmission rates after major operations',
   DATE '2026-04-05'),
  ('Pediatric Surgery Safety Checklist',
   'Pediatrics',
   'Multicenter audit of compliance and patient outcomes',
   DATE '2026-05-01'),
  ('Antimicrobial Stewardship in ICUs',
   'Infectious Diseases',
   'Strategies to reduce the prevalence of resistant organisms',
   DATE '2025-06-12'),
  ('Endoscopic Management of GI Bleeding',
   'Gastroenterology',
   'Comparative study of novel hemostatic agents',
   DATE '2025-07-08'),
  ('Stem-Cell Therapy for Spinal Cord Injury',
   'Regenerative Medicine',
   'Phase-II trial outcomes and quality-of-life metrics',
   DATE '2025-08-03'),
  ('AI in Radiology',
   'Radiology',
   'Detection accuracy compared with standard interpretation',
   DATE '2025-09-07'),
  ('Vaccination Strategies for the Elderly',
   'Public Health',
   'Efficacy of novel adjuvants and booster schedules',
   DATE '2025-10-15'),
  ('Mental Health Interventions Post-Surgery',
   'Psychology',
   'Effectiveness of CBT and mindfulness modules',
   DATE '2025-11-02'),
  ('Outbreak Response in Mass Casualty Situations',
   'Emergency Medicine',
   'Rapid triage protocols and resource allocation',
   DATE '2025-12-01');

-- 12 Researchers

INSERT INTO researchers (name, title, affiliation) VALUES
  ('Dr. A. Patel', 'Professor', 'Global Institute of Surgery'),
  ('Dr. B. Chang', 'Associate Professor', 'International Center for Health Studies'),
  ('Dr. C. Nguyen', 'Lecturer', 'Multinational Academy of Medicine'),
  ('Dr. D. Hernandez', 'Senior Lecturer', 'Universal Health Research Institute'),
  ('Dr. E. Kim', 'Assistant Professor', 'World Health Knowledge Base'),
  ('Dr. F. Ibrahim', 'Professor', 'United Medical Council'),
  ('Dr. G. Singh', 'Senior Lecturer', 'Global Surgical Forum'),
  ('Dr. H. Rivera', 'Associate Professor', 'International Medical Exchange'),
  ('Dr. I. Hassan', 'Lecturer', 'Cross-Cultural Health Forum'),
  ('Dr. J. Smith', 'Assistant Professor', 'Universal Health Consortium'),
  ('Dr. K. Lopez', 'Professor', 'International Health & Surgery Network'),
  ('Dr. L. Okafor', 'Senior Lecturer', 'Global Wellness Institute');

-- 12 Reviewers

INSERT INTO reviewers (name, title, affiliation) VALUES
  ('Dr. M. Carter', 'Professor', 'Global Health & Medicine Network'),
  ('Dr. N. O''Brien', 'Associate Professor', 'International Clinical Center'),
  ('Dr. O. Hassan', 'Lecturer', 'Multinational Health Forum'),
  ('Dr. P. Singh', 'Senior Lecturer', 'Universal Wellness Institute'),
  ('Dr. Q. Wang', 'Professor', 'Global Medical Association'),
  ('Dr. R. Martinez', 'Associate Professor', 'International Care Alliance'),
  ('Dr. S. Chen', 'Lecturer', 'Cross-Cultural Surgery Center'),
  ('Dr. T. Davis', 'Senior Lecturer', 'United Health Network'),
  ('Dr. U. Evans', 'Professor', 'Global Care Consortium'),
  ('Dr. V. Patel', 'Associate Professor', 'International Wellness Hub'),
  ('Dr. W. Rivera', 'Lecturer', 'Universal Medical Exchange'),
  ('Dr. X. Johnson', 'Senior Lecturer', 'Global Health Forum');

-- 20 Reviews

INSERT INTO reviews (title, content, rating, decision, article_id, reviewer_id) VALUES
  ('Review of Article 1',  'Review of Article 1',  4, 'ACCEPT',   1, 1),
  ('Review of Article 2',  'Review of Article 2',  5, 'ACCEPT',   2, 2),
  ('Review of Article 3',  'Review of Article 3',  3, 'REVISION', 3, 3),
  ('Review of Article 4',  'Review of Article 4',  5, 'ACCEPT',   4, 4),
  ('Review of Article 5',  'Review of Article 5',  2, 'REJECT',   5, 5),
  ('Review of Article 6',  'Review of Article 6',  4, 'ACCEPT',   6, 6),
  ('Review of Article 7',  'Review of Article 7',  3, 'REVISION', 7, 7),
  ('Review of Article 8',  'Review of Article 8',  5, 'ACCEPT',   8, 8),
  ('Review of Article 9',  'Review of Article 9',  1, 'REJECT',   9, 9),
  ('Review of Article 10', 'Review of Article 10', 4, 'REVISION', 10, 10),
  ('Review of Article 11', 'Review of Article 11', 5, 'ACCEPT',   11, 11),
  ('Review of Article 12', 'Review of Article 12', 2, 'REJECT',   12, 12),
  ('Review of Article 1',  'Review of Article 1',  3, 'ACCEPT',   1, 2),
  ('Review of Article 2',  'Review of Article 2',  4, 'REVISION', 2, 3),
  ('Review of Article 3',  'Review of Article 3',  5, 'ACCEPT',   3, 4),
  ('Review of Article 4',  'Review of Article 4',  3, 'REJECT',   4, 5),
  ('Review of Article 5',  'Review of Article 5',  4, 'ACCEPT',   5, 6),
  ('Review of Article 6',  'Review of Article 6',  2, 'REJECT',   6, 7),
  ('Review of Article 7',  'Review of Article 7',  5, 'ACCEPT',   7, 8),
  ('Review of Article 8',  'Review of Article 8',  4, 'REVISION', 8, 9);

-- 12 reviewer email addresses

INSERT INTO reviewer_emails (reviewer_id, emails) VALUES
  (1 , 'carter.professor@globalhealthnetwork.local'),
  (2 , 'obrien.assocprof@internationalclinicalcenter.local'),
  (3 , 'hassan.lecturer@multinationalhealthforum.local'),
  (4 , 'singh.senior@universalwellnessinstitute.local'),
  (5 , 'wang.professor@globalmedicalassociation.local'),
  (6 , 'martinez.assocprof@internationalcarealliance.local'),
  (7 , 'chen.lecturer@crossculturalcenter.local'),
  (8 , 'davis.senior@unitedhealthnetwork.local'),
  (9 , 'evans.professor@globalcareconsortium.local'),
  (10, 'patel.assocprof@internationalwellnesshub.local'),
  (11, 'rivera.lecturer@universalmedicalexchange.local'),
  (12, 'johnson.senior@globalhealthforum.local');

-- 12 researcher email addresses

INSERT INTO researcher_emails (researcher_id, emails) VALUES
  (1 , 'patel.professor@globalinstitutesurgery.local'),
  (2 , 'chang.assocprof@internationalhealthstudies.local'),
  (3 , 'nguyen.lecturer@multinationalacademyofmedicine.local'),
  (4 , 'hernandez.senior@universalhealthresearch.local'),
  (5 , 'kim.assistant@worldhealthknowledge.local'),
  (6 , 'ibrahim.professor@unitedmedicalcouncil.local'),
  (7 , 'singh.senior@globalforumofmedicine.local'),
  (8 , 'rivera.assocprof@internationalmedicalexchange.local'),
  (9 , 'hassan.lecturer@crossculturalhealthforum.local'),
  (10, 'smith.assistant@universalhealthconsortium.local'),
  (11, 'lopez.professor@internationalhealthnetwork.local'),
  (12, 'okafor.senior@globalwellnessinstitute.local');

-- 20 pseudo-random researcher-article links

INSERT INTO researcher_article (article_id, researcher_id) VALUES  
  (1,3), (1,12),
  (2,7), (2,9),
  (3,1), (3,5),
  (4,10), (4,1),
  (5,5), (5,4),
  (6,12), (6,3),
  (7,4), (7,11),
  (8,2), (8,8),
  (9,8), (9,10),
  (10,9), (10,6),
  (11,6), (11,2),
  (12,11), (12,7);

--12 pseudo-random reviewer-article links (note: this references the article id per your original FK)

INSERT INTO reviewer_article (article_id, reviewer_id) VALUES
  (1 , 8), 
  (2 , 11), 
  (3 , 4), 
  (4 , 2), 
  (5 , 9), 
  (6 , 12), 
  (7 , 5), 
  (8 , 1), 
  (9 , 6), 
  (10, 10), 
  (11, 3), 
  (12, 7);