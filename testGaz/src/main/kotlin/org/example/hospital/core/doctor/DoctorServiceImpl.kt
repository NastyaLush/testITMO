package org.example.hospital.core.doctor

import org.example.hospital.core.doctor.storage.DoctorRepository
import org.example.hospital.core.doctor.web.DoctorMapper
import org.example.hospital.core.exception.EntityNotFoundException
import org.example.hospital.generated.api.model.DoctorCreateDto
import org.example.hospital.generated.api.model.DoctorResponseDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DoctorServiceImpl(
    private val doctorMapper: DoctorMapper,
    private val doctorRepository: DoctorRepository
) : DoctorService {

    @Transactional(readOnly = true)
    override fun getAllDoctors(): List<DoctorResponseDto> {
        return doctorMapper.toResponseDtoList(doctorRepository.findAll())
    }

    @Transactional
    override fun createDoctor(doctorCreateDto: DoctorCreateDto): DoctorResponseDto {
        val doctor = doctorMapper.toEntity(doctorCreateDto)

        val saved = doctorRepository.save(doctor)

        return doctorMapper.toResponseDto(saved)
    }

    @Transactional
    override fun deleteDoctor(id: Long) {
        if (!doctorRepository.existsById(id)) {
            throw EntityNotFoundException("Doctor with this id was ot found")
        }
        doctorRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun getDoctorById(id: Long): DoctorResponseDto {
        val doctor = doctorRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Doctor with ID $id not found") }

        return doctorMapper.toResponseDto(doctor)
    }

    @Transactional
    override fun updateDoctor(id: Long, doctorCreateDto: DoctorCreateDto): DoctorResponseDto {
        val existing = doctorRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Doctor with ID $id not found") }

        val saved = existing.apply {
            this.id = id
            this.name = doctorCreateDto.name
        }

        return doctorMapper.toResponseDto(saved)
    }

}