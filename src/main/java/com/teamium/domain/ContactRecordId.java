package com.teamium.domain;

import java.io.Serializable;

import javax.persistence.Column;

public class ContactRecordId implements Serializable{
		
		/**
		 * CLass UID
		 */
		private static final long serialVersionUID = -4254553352265516925L;

		@Column(name="c_idperson")
		private Long idPerson;
		
		@Column(name="c_idrecord")
		private Long idRecord;

		/**
		 * @return the idPerson
		 */
		public Long getIdPerson() {
			return idPerson;
		}

		/**
		 * @param idPerson the idPerson to set
		 */
		public void setIdPerson(Long idPerson) {
			this.idPerson = idPerson;
		}

		/**
		 * @return the idRecord
		 */
		public Long getIdRecord() {
			return idRecord;
		}

		/**
		 * @param idRecord the idRecord to set
		 */
		public void setIdRecord(Long idRecord) {
			this.idRecord = idRecord;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((idPerson == null) ? 0 : idPerson.hashCode());
			result = prime * result + ((idRecord == null) ? 0 : idRecord.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ContactRecordId))
				return false;
			ContactRecordId other = (ContactRecordId) obj;
			if (idPerson == null) {
				if (other.idPerson != null)
					return false;
			} else if (!idPerson.equals(other.idPerson))
				return false;
			if (idRecord == null) {
				if (other.idRecord != null)
					return false;
			} else if (!idRecord.equals(other.idRecord))
				return false;
			return true;
		}
		
		
	}