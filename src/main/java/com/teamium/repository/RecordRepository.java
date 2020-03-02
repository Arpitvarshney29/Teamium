package com.teamium.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.Category;
import com.teamium.domain.MilestoneType;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.Layout;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;

@Repository
public interface RecordRepository<T> extends JpaRepository<Record, Long> {

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator ORDER By r.id ASC")
	List<Record> getRecords(@Param("discriminator") Class<?> discriminator);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator and r.id =:id")
	Record getRecordById(@Param("discriminator") Class<?> discriminator, @Param("id") Long id);

	@Query("SELECT r from Record r WHERE  TYPE(r) =:discriminator AND r.source IS NOT NULL AND r.source.id =:quotationId")
	Project getProjectByQuotationId(@Param("discriminator") Class<?> discriminator,
			@Param("quotationId") Long quotationId);

	@Query("SELECT COUNT (r) from Record r WHERE r.company.id =:companyId")
	int findProjectCountByCompany(@Param("companyId") long companyId);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND (r.status IS NOT NULL AND r.status.key = 'In Progress' ) ")
	List<Record> getRecordsByProgress(@Param("discriminator") Class<?> discriminator);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.status IS NOT NULL AND (r.status.key = 'To Do' OR r.status.key = 'Done')")
	List<Record> getRecordsToDoAndDone(@Param("discriminator") Class<?> discriminator);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.status IS NOT NULL AND NOT (r.status.key = 'In Progress')")
	List<Record> getRecordsNotInProgress(@Param("discriminator") Class<?> discriminator);

	@Query("SELECT r FROM Record r WHERE r.company.id = :companyId AND TYPE(r) = :discriminator")
	List<Record> findRecordByCompany(@Param("companyId") long companyId,
			@Param("discriminator") Class<?> discriminator);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.status IS NOT NULL AND "
			+ " NOT ( (( r.status.key = 'To Do' OR r.status.key = 'In Progress') AND r.financialStatus = 'Rejected') OR "
			+ "(r.status.key = 'Done' AND r.financialStatus = 'Approved') )")
	List<Record> getRecordsShowByStatus(@Param("discriminator") Class<?> discriminator);

	@Query("SELECT COUNT(r) from Record r WHERE r.program.id =:programId AND (r.status IS NOT NULL AND r.status.key = 'Done' AND r.financialStatus = 'Approved') ")
	int findSessionsCountByProgram(@Param("programId") long programId);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.title =:title")
	Record getLayoutByTitle(@Param("discriminator") Class<?> discriminator, @Param("title") String title);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.source IS NOT NULL AND r.source.id =:projectId")
	Record getLayoutByProjectId(@Param("discriminator") Class<?> discriminator, @Param("projectId") long projectId);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator")
	List<Record> getLayouts(@Param("discriminator") Class<?> discriminator);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.source IS NOT NULL")
	List<Layout> getLayoutsByDiscriminatorAndSource(@Param("discriminator") Class<?> discriminator);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.source IS NOT NULL AND r.source.id =:recordId")
	Layout getLayoutsByDiscriminatorAndSource(@Param("discriminator") Class<?> discriminator,
			@Param("recordId") long recordId);

	@Query("SELECT r from Quotation r WHERE r.company.id =:companyId")
	List<Quotation> findProjectByCompanyId(@Param("companyId") long companyId);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.source IS NOT NULL AND r.source.id =:projectId")
	List<Record> findRecordByProjectId(@Param("discriminator") Class<?> discriminator,
			@Param("projectId") long projectId);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.status IS NOT NULL AND (r.status.key = 'To Do' OR r.status.key = 'In Progress' ) ")
	List<Record> getRecordsByTodoORProgress(@Param("discriminator") Class<?> discriminator);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.source IS NOT NULL AND r.source.id =:projectId AND r.company IS NOT NULL AND r.company.id =:supplierId")
	Record getRecordByProjectAndSupplier(@Param("discriminator") Class<?> discriminator,
			@Param("projectId") long projectId, @Param("supplierId") long supplierId);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND (r.status IS NOT NULL AND r.status.key IN (:status) AND r.financialStatus = 'Approved')")
	List<Record> findRecordsByStatus(@Param("discriminator") Class<?> discriminator,
			@Param("status") List<String> status);

	@Query("SELECT p.title from Project p WHERE p.id =:projectId")
	String findProjectTitleId(@Param("projectId") long projectId);

	@Query("SELECT r from Record r WHERE r.category IS NOT NULL AND r.category =:category")
	List<Record> findRecordByCategory(@Param("category") Category category);

	@Query("SELECT r FROM Record r join r.dueDates dueDateList WHERE :dueDate IN (dueDateList.type)")
	List<Record> findRecordByDueDate(@Param("dueDate") MilestoneType dueDate);

	@Query("SELECT r FROM Record r WHERE TYPE(r) =:discriminator AND r.date IS NOT NULL AND ((CAST(r.date AS date)>=CAST(:start AS date)) AND (CAST(r.date AS date)<=CAST(:end AS date))) ORDER By r.id ASC")
	List<Record> findProjectVolume(@Param("discriminator") Class<?> discriminator, @Param("start") Calendar start,
			@Param("end") Calendar end);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.status IS NOT NULL AND (r.status.key = 'To Do') AND r.date IS NOT NULL AND ((CAST(r.date AS date)>=CAST(:start AS date)) AND (CAST(r.date AS date)<=CAST(:end AS date))) ORDER By r.id ASC")
	List<Record> findRecordsByToDoAndDateRange(@Param("discriminator") Class<?> discriminator,
			@Param("start") Calendar start, @Param("end") Calendar end);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.status IS NOT NULL AND (r.status.key = 'In Progress' OR r.status.key = 'Done') AND r.date IS NOT NULL AND ((CAST(r.date AS date)>=CAST(:start AS date)) AND (CAST(r.date AS date)<=CAST(:end AS date))) ORDER By r.id ASC")
	List<Record> findRecordsByInProgressAndDoneAndDateRange(@Param("discriminator") Class<?> discriminator,
			@Param("start") Calendar start, @Param("end") Calendar end);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.status IS NOT NULL AND r.status.key=:status AND r.date IS NOT NULL AND ((CAST(r.date AS date)>=CAST(:start AS date)) AND (CAST(r.date AS date)<=CAST(:end AS date))) ORDER By r.id ASC")
	List<Record> getRecordsByDateRangeAndStatus(@Param("discriminator") Class<?> discriminator,
			@Param("start") Calendar start, @Param("end") Calendar end, @Param("status") String status);

	@Query("SELECT SUM (r.totalPriceIVAT) AS revenue from Record r WHERE TYPE(r) =:discriminator AND r.financialStatus IS NOT NULL AND r.financialStatus=:financialStatus AND r.date IS NOT NULL AND ((CAST(r.date AS date)>=CAST(:start AS date)) AND (CAST(r.date AS date)<=CAST(:end AS date)))")
	Float getProjectRevenueByDateRangeAndStatus(@Param("discriminator") Class<?> discriminator,
			@Param("start") Calendar start, @Param("end") Calendar end,
			@Param("financialStatus") String financialStatus);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.financialStatus IS NOT NULL AND r.financialStatus=:financialStatus AND r.date IS NOT NULL AND ((CAST(r.date AS date)>=CAST(:start AS date)) AND (CAST(r.date AS date)<=CAST(:end AS date))) ORDER By r.id ASC")
	List<Record> getProjectListByDateRangeAndStatus(@Param("discriminator") Class<?> discriminator,
			@Param("start") Calendar start, @Param("end") Calendar end,
			@Param("financialStatus") String financialStatus);

	@Query("SELECT SUM (r.totalPriceIVAT) AS revenue from Record r WHERE TYPE(r) =:discriminator AND r.date IS NOT NULL AND ((CAST(r.date AS date)>=CAST(:start AS date)) AND (CAST(r.date AS date)<=CAST(:end AS date)))")
	Float getProjectRevenueByDateRange(@Param("discriminator") Class<?> discriminator, @Param("start") Calendar start,
			@Param("end") Calendar end);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.date IS NOT NULL AND ((CAST(r.date AS date)>=CAST(:start AS date)) AND (CAST(r.date AS date)<=CAST(:end AS date))) ORDER By r.id ASC")
	List<Record> getRecordsByDateRange(@Param("discriminator") Class<?> discriminator, @Param("start") Calendar start,
			@Param("end") Calendar end);

	@Query("SELECT r from Record r WHERE r.entity IS NOT NULL AND r.entity.id=:companyId")
	List<Record> getRecordsByCompany(@Param("companyId") Long companyId);

	@Query("SELECT r from Record r WHERE TYPE(r) =:discriminator AND r.modifiedDate IS NOT NULL AND ((r.modifiedDate>=:start)) ORDER By r.id ASC")
	List<Record> getRecordsByDateTimeRange(@Param("discriminator") Class<?> discriminator,
			@Param("start") Calendar start);

	List<Record> findByCurrencyIgnoreCase(String currency);

}
