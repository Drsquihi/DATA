 # DATA

question 1:
SELECT t.teacher_epita_email,
       COUNT(*) AS nb_sessions
FROM sessions s
JOIN teachers t ON s.session_prof_ref = t.teacher_epita_email
GROUP BY t.teacher_epita_email
HAVING COUNT(*) >= 10
ORDER BY nb_sessions DESC, t.teacher_epita_email ASC
LIMIT 10;

question 2:
SELECT tr.track_code,
       COUNT(*) AS nb_students
FROM students s
JOIN tracks tr ON s.student_population_code_ref = tr.track_code
GROUP BY tr.track_code
ORDER BY nb_students DESC, tr.track_code ASC;

question 3:
SELECT t.teacher_epita_email,
       t.teacher_study_level
FROM teachers t
WHERE t.teacher_dept_ref = 'CS'
ORDER BY t.teacher_study_level DESC, t.teacher_epita_email ASC;

question 4:
SELECT room_code,
       building,
       capacity,
       room_type
FROM rooms
WHERE capacity >= 100
ORDER BY capacity DESC, room_code ASC;

question 5:
SELECT co.course_code, co.course_rev, co.course_name
FROM courses co
LEFT JOIN enrollments e ON e.enrollment_course_code_ref = co.course_code AND e.enrollment_course_rev_ref = co.course_rev
WHERE e.enrollment_student_ref IS NULL
ORDER BY co.course_code, co.course_rev;

question 6:
SELECT s.student_population_code_ref AS track,
       s.student_population_year_ref AS year,
       s.student_population_period_ref AS period,
       COUNT(DISTINCT s.student_epita_email) AS nb_students,
       ROUND(AVG(g.grade_score),2) AS avg_grade
FROM students s
JOIN grades g ON g.grade_student_epita_email_ref = s.student_epita_email
GROUP BY s.student_population_code_ref, s.student_population_year_ref, s.student_population_period_ref
ORDER BY avg_grade DESC, track ASC, year ASC, period ASC
LIMIT 10;

Q7 — Software Engineering students and their city
sqlSELECT s.student_epita_email,
       c.contact_last_name,
       c.contact_first_name,
       c.contact_city
FROM students s
JOIN contacts c ON s.student_contact_ref = c.contact_email
WHERE s.student_population_code_ref = 'SE'
ORDER BY c.contact_last_name, c.contact_first_name, s.student_epita_email
LIMIT 10;

Q8 — Distinct contact cities
sqlSELECT DISTINCT contact_city FROM contacts ORDER BY contact_city LIMIT 10;

Q9 — Students above the global average
sqlWITH student_avg AS (
    SELECT grade_student_epita_email_ref AS student, AVG(grade_score) AS avg_score
    FROM grades
    GROUP BY grade_student_epita_email_ref
),
global_avg AS (
    SELECT AVG(grade_score) AS avg_score FROM grades
)
SELECT sa.student, ROUND(sa.avg_score,2) AS avg_score
FROM student_avg sa, global_avg ga
WHERE sa.avg_score > ga.avg_score
ORDER BY sa.avg_score DESC, sa.student ASC
LIMIT 10;

Q10 — Top 2 students of each course
sqlWITH student_course_avg AS (
    SELECT g.grade_course_code_ref AS course_code,
           g.grade_course_rev_ref AS course_rev,
           g.grade_student_epita_email_ref AS student,
           AVG(g.grade_score) AS avg_score
    FROM grades g
    GROUP BY g.grade_course_code_ref, g.grade_course_rev_ref, g.grade_student_epita_email_ref
),
ranked AS (
    SELECT sca.*, RANK() OVER (PARTITION BY sca.course_code, sca.course_rev ORDER BY sca.avg_score DESC) AS rnk
    FROM student_course_avg sca
)
SELECT co.course_name, r.student, ROUND(r.avg_score,2) AS avg_score, r.rnk
FROM ranked r
JOIN courses co ON co.course_code = r.course_code AND co.course_rev = r.course_rev
WHERE r.rnk <= 2
ORDER BY co.course_name, r.rnk, r.student
LIMIT 10;

Q11 — Attendance rate per course
sqlSELECT co.course_name,
       COUNT(*) AS nb_records,
       ROUND(100.0 * SUM(a.attendance_presence) / COUNT(*), 2) AS attendance_rate
FROM attendance a
JOIN courses co ON co.course_code = a.attendance_course_ref AND co.course_rev = a.attendance_course_rev
GROUP BY co.course_code, co.course_rev, co.course_name
HAVING COUNT(*) >= 50
ORDER BY attendance_rate DESC, co.course_name ASC
LIMIT 10;

Q12 — Prospective (selected) students
sqlSELECT student_epita_email,
       student_population_code_ref AS track,
       student_population_year_ref AS year
FROM students
WHERE student_enrollment_status = 'selected'
ORDER BY student_epita_email ASC
LIMIT 10;

Q13 — Best-scoring course of each department
sqlWITH course_avg AS (
    SELECT co.course_code, co.course_rev, co.course_name, co.course_dept_ref,
           AVG(g.grade_score) AS avg_score
    FROM grades g
    JOIN courses co ON co.course_code = g.grade_course_code_ref AND co.course_rev = g.grade_course_rev_ref
    GROUP BY co.course_code, co.course_rev, co.course_name, co.course_dept_ref
),
ranked AS (
    SELECT ca.*, RANK() OVER (PARTITION BY ca.course_dept_ref ORDER BY ca.avg_score DESC) AS rnk
    FROM course_avg ca
)
SELECT d.dept_name, r.course_name, ROUND(r.avg_score,2) AS avg_score
FROM ranked r
JOIN departments d ON d.dept_code = r.course_dept_ref
WHERE r.rnk = 1
ORDER BY avg_score DESC, d.dept_name ASC;

Q14 — Courses with their department name
sqlSELECT co.course_code, co.course_rev, co.course_name, d.dept_name
FROM courses co
JOIN departments d ON d.dept_code = co.course_dept_ref
ORDER BY co.course_name, co.course_code, co.course_rev
LIMIT 10;

Q15 — Exams per type
sqlSELECT exam_type, COUNT(*) AS nb_exams
FROM exams
GROUP BY exam_type
ORDER BY nb_exams DESC, exam_type ASC;

Q16 — Course and teacher count per department
sqlSELECT d.dept_name,
       (SELECT COUNT(*) FROM courses co WHERE co.course_dept_ref = d.dept_code) AS nb_courses,
       (SELECT COUNT(*) FROM teachers t WHERE t.teacher_dept_ref = d.dept_code) AS nb_teachers
FROM departments d
ORDER BY nb_courses DESC, d.dept_name ASC;

Q17 — Sessions per room
sqlSELECT r.room_code, COUNT(s.session_room_ref) AS nb_sessions
FROM rooms r
LEFT JOIN sessions s ON s.session_room_ref = r.room_code
GROUP BY r.room_code
ORDER BY nb_sessions DESC, r.room_code ASC
LIMIT 10;

Q18 — Full prerequisite chain (transitive closure)
sqlWITH RECURSIVE prereq_chain AS (
    SELECT course_code_ref AS course_code, course_rev_ref AS course_rev,
           prereq_course_code_ref AS prereq_code, prereq_course_rev_ref AS prereq_rev,
           1 AS steps
    FROM course_prerequisites
    UNION ALL
    SELECT pc.course_code, pc.course_rev,
           cp.prereq_course_code_ref, cp.prereq_course_rev_ref,
           pc.steps + 1
    FROM prereq_chain pc
    JOIN course_prerequisites cp
      ON cp.course_code_ref = pc.prereq_code AND cp.course_rev_ref = pc.prereq_rev
)
SELECT course_code, course_rev, prereq_code, prereq_rev, steps
FROM prereq_chain
ORDER BY course_code, steps, prereq_code
LIMIT 10;

Q19 — Grade-band breakdown per course
sqlSELECT co.course_name,
       SUM(CASE WHEN g.grade_score < 10 THEN 1 ELSE 0 END) AS below_10,
       SUM(CASE WHEN g.grade_score >= 10 AND g.grade_score <= 13 THEN 1 ELSE 0 END) AS from_10_to_13,
       SUM(CASE WHEN g.grade_score >= 14 THEN 1 ELSE 0 END) AS at_least_14,
       COUNT(*) AS total
FROM grades g
JOIN courses co ON co.course_code = g.grade_course_code_ref AND co.course_rev = g.grade_course_rev_ref
GROUP BY co.course_code, co.course_rev, co.course_name
ORDER BY total DESC, co.course_name ASC
LIMIT 10;

Q20 — Attendance rate vs average grade
sqlWITH att AS (
    SELECT attendance_student_ref AS student,
           100.0 * SUM(attendance_presence) / COUNT(*) AS att_rate
    FROM attendance
    GROUP BY attendance_student_ref
),
gr AS (
    SELECT grade_student_epita_email_ref AS student,
           AVG(grade_score) AS avg_score
    FROM grades
    GROUP BY grade_student_epita_email_ref
)
SELECT gr.student, ROUND(att.att_rate,2) AS attendance_rate, ROUND(gr.avg_score,2) AS avg_grade
FROM gr
JOIN att ON att.student = gr.student
ORDER BY attendance_rate DESC, avg_grade DESC, gr.student ASC
LIMIT 10;

Q21 — AI-department courses, longest first
sqlSELECT course_code, course_name, duration
FROM courses
WHERE course_dept_ref = 'AI'
ORDER BY duration DESC, course_code ASC;

Q22 — Top Project marks (above 15)
sqlSELECT g.grade_student_epita_email_ref AS student,
       co.course_name,
       g.grade_score
FROM grades g
JOIN courses co ON co.course_code = g.grade_course_code_ref AND co.course_rev = g.grade_course_rev_ref
WHERE g.grade_exam_type_ref = 'Project' AND g.grade_score > 15
ORDER BY g.grade_score DESC, g.grade_student_epita_email_ref ASC, co.course_name ASC
LIMIT 10;
