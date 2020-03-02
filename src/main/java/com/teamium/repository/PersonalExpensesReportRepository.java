package com.teamium.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.PersonalExpensesReport;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.dto.PersonalExpensesReportDTO;

@Repository
public interface PersonalExpensesReportRepository extends JpaRepository<PersonalExpensesReport, Long> {

	@Query("SELECT new com.teamium.dto.PersonalExpensesReportDTO(ex) FROM PersonalExpensesReport ex")
	List<PersonalExpensesReportDTO> findAllExpensesReports();

	@Query("SELECT new com.teamium.dto.PersonalExpensesReportDTO(ex) FROM PersonalExpensesReport ex where ex.staffMember =:staffMember ORDER BY ex.id")
	List<PersonalExpensesReportDTO> findByStaffMember(@Param("staffMember") StaffMember staffMember);
	
	@Query("SELECT new com.teamium.dto.PersonalExpensesReportDTO(ex) FROM PersonalExpensesReport ex WHERE ex.staffMember =:staffMember AND(ex.date >=:from AND ex.date <=:to) ")
	List<PersonalExpensesReportDTO> findAllExpensesReportsBetween(@Param("from") Calendar from,@Param("to") Calendar to,@Param("staffMember") StaffMember staffMember);
	
}
